package com.clincmangment.controller

import com.clincmangment.repository.dto.NurseForm
import com.clincmangment.repository.model.User
import com.clincmangment.service.PatientService
import com.clincmangment.service.UserServiceImpl
import com.clincmangment.service.VisitService
import com.clincmangment.utils.Role
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDateTime

@Controller
@RequestMapping("/doctor")
class DoctorController(
    private val visitService: VisitService,
    private val userService: UserServiceImpl,
    private val patientService: PatientService,
    private val httpSession: HttpSession
) {

    private val logger = LoggerFactory.getLogger(DoctorController::class.java)

    // لوحة التحكم - زيارات اليوم الجارية حسب العيادة
    @GetMapping("/dashboard/{phone}")
    fun doctorDashboard(@PathVariable phone: String, model: Model): String {
        return try {
            val doctor = userService.findByPhone(phone).orElseThrow { IllegalArgumentException("Doctor not found") }
            val clinicId = doctor.clinic.id!!
            val currentVisits = visitService.getVisitsByDoctorAndStatus(doctor.id!!, "جاري الكشف", clinicId)

            model.addAttribute("doctor", doctor)
            model.addAttribute("currentVisits", currentVisits)
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
            val clinicId = (httpSession.getAttribute("loggedUser") as User).clinic.id!!

            // تحقق إن الزيارة تتبع نفس العيادة
            if (visit.clinic.id != clinicId) {
                model.addAttribute("errorMessage", "ليس لديك صلاحية لرؤية هذه الزيارة")
                return "error/custom_error"
            }

            val patient = visit.patient
            val previousVisits = visitService.getVisitsByPatient(patient.id!!, clinicId)

            model.addAttribute("visit", visit)
            model.addAttribute("patient", patient)
            model.addAttribute("previousVisits", previousVisits)
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
        @RequestParam prescription: String,
        @RequestParam(required = false) scheduledConsultation: String?,
        model: Model
    ): String {
        return try {
            val visit = visitService.getVisitById(visitId)
                .orElseThrow { IllegalArgumentException("Visit not found") }

            val doctor = httpSession.getAttribute("loggedUser") as? User
                ?: throw IllegalArgumentException("User not logged in")

            if (visit.clinic.id != doctor.clinic.id) {
                throw IllegalArgumentException("Visit does not belong to your clinic")
            }

            visit.diagnosis = diagnosis
            visit.prescription = prescription
            if (!scheduledConsultation.isNullOrBlank()) {
                visit.scheduledConsultation = LocalDateTime.parse(scheduledConsultation)
            }

            visitService.save(visit)
            "redirect:/doctor/visit/${visitId}"
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
        val doctor = session.getAttribute("loggedUser") as? User
            ?: throw IllegalArgumentException("User not logged in")

        val nurse = userService.createUser(
            username = nurseForm.fullName,
            rawPassword = nurseForm.password ?: "1234",
            role = Role.NURSE,
            fullName = nurseForm.fullName,
            phone = nurseForm.phone,
            clinic = doctor.clinic
        )

        redirectAttributes.addFlashAttribute("success", "تمت إضافة الممرضة بنجاح")
        return "redirect:/doctor/add-nurse"
    }




}
