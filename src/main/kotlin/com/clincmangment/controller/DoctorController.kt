package com.clincmangment.controller

import com.clincmangment.model.User
import com.clincmangment.repository.dto.NurseForm
import com.clincmangment.service.ClinicService
import com.clincmangment.service.ClinicServiceService
import com.clincmangment.service.ServiceVisitService
import com.clincmangment.service.UserServiceImpl
import com.clincmangment.service.VisitService
import com.clincmangment.utils.Role
import com.clincmangment.utils.SubscriptionType
import com.clincmangment.utils.VisitType
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.security.Principal
import java.time.LocalDate
import java.time.LocalDateTime


@Controller
@RequestMapping("/doctor")
class DoctorController(
    private val visitService: VisitService,
    private val userService: UserServiceImpl,
    private val httpSession: HttpSession,
    private val clinicServiceService: ClinicServiceService,
    private val clinicService: ClinicService,
    private val serviceVisitSrevice: ServiceVisitService
) {

    private val logger = LoggerFactory.getLogger(DoctorController::class.java)

    // لوحة التحكم - زيارات اليوم الجارية حسب العيادة
    @GetMapping("/dashboard")
    fun doctorDashboard(model: Model): String {
        val user = httpSession.getAttribute("loggedUser") as? User ?: return "redirect:/login"
        val isSubscriptionAdvanced = user.clinic.subscriptionType == SubscriptionType.ADVANCED
        model.addAttribute("isSubscriptionAdvanced", isSubscriptionAdvanced)

        val subscriptionActive = clinicService.isSubscriptionActive(user.clinic)
        model.addAttribute("subscriptionExpired", !subscriptionActive)
        return try {
            val phone = user.phone
            val doctor = userService.findByPhone(phone!!)
//                .orElseThrow { IllegalArgumentException("Doctor not found") }
            val clinicId = doctor!!.clinic.id!!
            val currentVisits = visitService.getVisitsByDoctorAndStatus(doctor.id!!, "جاري الكشف", clinicId)
            // ✅ إجماليات الحالات لكل نوع
            val allVisits = visitService.getVisitsByDoctor(doctor.id!!, clinicId)
            val stats = allVisits.groupingBy { it.status ?: "غير محدد" }.eachCount()

            val total = allVisits.size
            val done = stats["تم الكشف"] ?: 0
            val canceled = stats["ملغاة"] ?: 0
            val pending = stats["في الانتظار"] ?: 0
            val current = stats["جاري الكشف"] ?: 0
            val startOfDay = LocalDate.now().atStartOfDay()
            val endOfDay = startOfDay.plusDays(1).minusSeconds(1)
            val todayVisits = visitService.findVisitsByDoctorAndDate(
                doctor.id!!,
                startOfDay, endOfDay, clinicId
            )

            val totalRevenue = todayVisits.filter { it.status != "ملغاة" }.sumOf {
                when (it.visitType!!) {
                    VisitType.CHECKUP -> doctor.clinic.checkUpPrice
                    VisitType.CONSULTATION -> doctor.clinic.followUpPrice
                }
            }
            model.addAttribute("totalRevenue", totalRevenue)
            model.addAttribute("doctor", doctor)
            model.addAttribute("currentVisits", currentVisits)
            model.addAttribute("totalCount", total)
            model.addAttribute("doneCount", done)
            model.addAttribute("canceledCount", canceled)
            model.addAttribute("pendingCount", pending)
            model.addAttribute("currentCount", current)
            return "doctor/dashboard"
        } catch (ex: Exception) {
            logger.error("Error loading doctor dashboard: ${ex.message}")
            model.addAttribute("errorMessage", "حدث خطأ أثناء تحميل لوحة التحكم. برجاء المحاولة لاحقًا.")
            "error/custom_error"
        }
    }

    // عرض الكشف الجاري مع التاريخ السابق للزيارات لنفس العيادة فقط
    @GetMapping("/visit/{visitId}")
    fun viewCurrentVisit(@PathVariable visitId: Long, model: Model): String {
        return try {
            val visit = visitService.getVisitById(visitId).orElseThrow { IllegalArgumentException("Visit not found") }
            val loggedUser = (httpSession.getAttribute("loggedUser") as User)

            // تحقق إن الزيارة تتبع نفس العيادة
            if (visit.clinic!!.id != loggedUser.clinic.id) {
                model.addAttribute("errorMessage", "ليس لديك صلاحية لرؤية هذه الزيارة")
                return "error/custom_error"
            }

            val patient = visit.patient
            val previousVisits = visitService.getVisitsByPatient(patient!!.id!!, loggedUser.clinic.id!!)
            val services = clinicServiceService.getDoctorServices(loggedUser.id!!)
            model.addAttribute("visit", visit)
            model.addAttribute("patient", patient)
            model.addAttribute("previousVisits", previousVisits)
            model.addAttribute("services", services)
            return "doctor/current_visit"
        } catch (ex: Exception) {
            logger.error("Error viewing visit: ${ex.message}")
            model.addAttribute("errorMessage", "لم يتم العثور على الزيارة المطلوبة.")
            "error/custom_error"
        }
    }

    // تحديث بيانات الزيارة
    @PostMapping("/visit/update")
    fun updateVisit(
        @RequestParam visitId: Long,
        @RequestParam diagnosis: String,
        @RequestParam prescription: String, // هذا سيحتوي على JSON الآن
        @RequestParam(required = false) scheduledConsultation: String?,
        @RequestParam(required = false, defaultValue = "0") servicesTotal: Double,
        model: Model
    ): String {
        return try {
            val visit = visitService.getVisitById(visitId)
                .orElseThrow { IllegalArgumentException("Visit not found") }
            val doctor = httpSession.getAttribute("loggedUser") as? User
                ?: throw IllegalArgumentException("User not logged in")
            if (visit.clinic!!.id != doctor.clinic.id) {
                throw IllegalArgumentException("Visit does not belong to your clinic")
            }
            when(visit.visitType){
                VisitType.CHECKUP -> {
                    visit.visitPrice = doctor.clinic.checkUpPrice + servicesTotal
                }
                VisitType.CONSULTATION -> visit.visitPrice += doctor.clinic.followUpPrice+servicesTotal
                else -> {}
            }
            // التحقق من أن الروشتة هي JSON صالح
            val isJsonPrescription = prescription.trim().startsWith("[") || prescription.trim().startsWith("{")

            if (isJsonPrescription) {
                // حفظ الروشتة كـ JSON
                visit.prescription = prescription
            } else {
                // للحفاظ على التوافق مع الإصدار القديم
                visit.prescription = prescription
            }

            visit.diagnosis = diagnosis
            visit.status = "تم الكشف"

            if (!scheduledConsultation.isNullOrBlank()) {
                visit.scheduledConsultation = LocalDateTime.parse(scheduledConsultation)
            }

            visitService.save(visit)
            "redirect:/doctor/dashboard"
        } catch (ex: Exception) {
            logger.error("Error updating visit: ${ex.message}")
            model.addAttribute("errorMessage", "حدث خطأ أثناء تحديث بيانات الزيارة.")
            "error/custom_error"
        }
    }

    // إضافة ممرضة جديدة بنفس العيادة
    @GetMapping("/add-nurse")
    fun showAddNurseForm(model: Model): String {
        model.addAttribute("nurseForm", NurseForm())
        return "doctor/add_nurse"
    }

    @PostMapping("/add-nurse")
    fun addNurse(
        @ModelAttribute nurseForm: NurseForm,
        redirectAttributes: RedirectAttributes,
        session: HttpSession
    ): String {
        try {
            val doctor = session.getAttribute("loggedUser") as? User
                ?: throw IllegalArgumentException("User not logged in")

            userService.createUser(
                username = nurseForm.fullName,
                rawPassword = nurseForm.password ?: "1234",
                role = Role.NURSE,
                fullName = nurseForm.fullName,
                phone = nurseForm.phone,
                clinic = doctor.clinic
            )

            redirectAttributes.addFlashAttribute("success", "تمت إضافة الممرضة بنجاح")
            return "redirect:/doctor/add-nurse"
        } catch (e: Exception) {
            e.printStackTrace()
            redirectAttributes.addFlashAttribute("error", "رقم الهاتف موجود مسيقا")
            return "redirect:/doctor/add-nurse"
        }
    }

    // DoctorController.kt
    @GetMapping("/settings")
    fun showClinicSettings(
        model: Model, session: HttpSession,
        principal: Principal
    ): String {
        val loggedUser = session.getAttribute("loggedUser") as User
        model.addAttribute(
            "services",
            clinicServiceService.getDoctorServices(loggedUser.id!!)
        )

        val clinic = clinicService.findClinicById(loggedUser.clinic.id!!)
        model.addAttribute("clinic", clinic)
        return "doctor/services-list"
    }

    @PostMapping("/settings")
    fun updateClinicSettings(
        @RequestParam consultationPrice: Double,
        @RequestParam followUpPrice: Double,
        redirectAttributes: RedirectAttributes,
        session: HttpSession
    ): String {
        val doctor = session.getAttribute("loggedUser") as? User
            ?: throw IllegalArgumentException("User not logged in")

        clinicService.updateClinicPrices(doctor.id, consultationPrice, followUpPrice)
        session.setAttribute("loggedUser", doctor)
        redirectAttributes.addFlashAttribute("success", "تم تحديث الأسعار بنجاح")
        return "redirect:/doctor/dashboard"
    }


    // مثال في DoctorVisitController
    @PostMapping("/visit/service/delete")
    @ResponseBody
    fun deleteVisitService(@RequestParam visitServiceId: Long?): ResponseEntity<*> {

        try{
            serviceVisitSrevice.deleteById(visitServiceId)
            println("***************************************")
            println("$visitServiceId")
            println("***************************************")
        }  catch (e: Exception){
            e.printStackTrace()
        }
        return ResponseEntity.ok().build<Any?>()

    }

    @GetMapping("/visit/services")
    @ResponseBody
    fun list(): List<com.clincmangment.model.ClinicService> {
        val loggedUser = httpSession.getAttribute("loggedUser") as User
        return clinicServiceService.getDoctorServices(loggedUser.id!!)
    }

    @PostMapping("/visit/service/add")
    fun visitAdd(
        @RequestParam visitId: Long, @RequestParam serviceId: Long,
        @RequestParam price: Double,
        redirectAttributes: RedirectAttributes
    ): String {
        val loggedUser = httpSession.getAttribute("loggedUser") as User
        serviceVisitSrevice.save(
            com.clincmangment.model.VisitService(
                visit = visitService.getVisitById(visitId).get(),
                service = clinicServiceService.getClinicServiceById(serviceId),
                price = price
            )
        )
        redirectAttributes.addFlashAttribute("success", "تم إضافة الخدمة بنجاح")
        return "redirect:/doctor/visit/${visitId}"
    }

    @GetMapping("/visit/service/list")
    @ResponseBody
    fun getVisitServices(@RequestParam visitId: Long): ResponseEntity<List<com.clincmangment.model.VisitService>> {

        // 1. جلب الخدمات من قاعدة البيانات بناءً على معرف الزيارة
        val visitServices = serviceVisitSrevice.findByVisitId(visitId) // افترضنا وجود هذه الدالة في الخدمة

        // 2. إرجاع القائمة مع حالة HTTP 200 (OK)
        return ResponseEntity.ok(visitServices)
    }
}
