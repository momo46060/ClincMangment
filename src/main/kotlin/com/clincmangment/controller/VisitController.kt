package com.clincmangment.controller

import com.clincmangment.repository.dto.VisitForm
import com.clincmangment.model.User
import com.clincmangment.service.ClinicServiceService
import com.clincmangment.service.PatientService
import com.clincmangment.service.ServiceVisitService
import com.clincmangment.service.UserServiceImpl
import com.clincmangment.service.VisitService
import com.clincmangment.utils.EditVisitForm
import com.clincmangment.utils.Role
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.format.DateTimeFormatter

@Controller
@RequestMapping("/visits")
class VisitController(
    private val visitService: VisitService,
    private val patientService: PatientService,
    private val userService: UserServiceImpl,
    private val clinicVisitService: ClinicServiceService,

    private val serviceVisitService: ServiceVisitService,

    ) {

    private val logger = LoggerFactory.getLogger(VisitController::class.java)

    @GetMapping("/list")
    fun listVisits(
        model: Model,
        session: HttpSession,
    ): String {
        try {
            val loggedUser = session.getAttribute("loggedUser") as? User
                ?: return "redirect:/login"
            val visits = visitService.getVisitsByDoctor(
                doctorId = loggedUser.id!!,
                clinicId = loggedUser.clinic.id).sortedBy { it.id }
            model.addAttribute("doctor",loggedUser)
            model.addAttribute("visits", visits)

            return "visits/list"
        } catch (e: Exception) {
            return "redirect:/login"
        }
    }

    @GetMapping("/edit/{visitId}")
    fun showEditVisitForm(@PathVariable visitId: Long, model: Model): String {
        val visit = visitService.getVisitById(visitId)
            .orElseThrow { IllegalArgumentException("Visit not found") }
        // نموذج التعديل
        val visitForm = EditVisitForm(
            visitId = visit.id!!,
            patientCode = visit.patient?.patientCode ?: "",
            diagnosis = visit.diagnosis ?: "",
            prescription = visit.prescription ?: "", // هذا سيحتوي على JSON
            scheduledConsultation = visit.scheduledConsultation?.
            format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) ?: ""
        )
        val services = clinicVisitService.getDoctorServices(visit.doctor!!.id!!)
        val visitServices = serviceVisitService.findByVisitId(visit.id!!)
        model.addAttribute("services",services)
        model.addAttribute("visit",visit)
        model.addAttribute("visitServices",visitServices)
        model.addAttribute("visitForm", visitForm)
        model.addAttribute("patientName", visit.patient?.user?.fullName ?: "")
        model.addAttribute("patientCode", visit.patient?.patientCode ?: "")
        model.addAttribute("visitDate", visit.visitDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
        model.addAttribute("doctorUsername", visit.doctor?.fullName?: "")

        return "visits/edit"
    }

    @GetMapping("/new/{patientCode}")
    fun newVisitForm(
        @PathVariable patientCode: String,
        model: Model,
        session: HttpSession,
        redirectAttributes: RedirectAttributes
    ): String {
        return try {
            val loggedUser = session.getAttribute("loggedUser") as? User
                ?: return "redirect:/login"

            val patient = patientService.findByPatientCodeAndClinic(patientCode, loggedUser.clinic)
                ?: throw IllegalArgumentException("Patient not found in your clinic")

            val doctors = userService.getUsersByRoleAndClinic(
                Role.DOCTOR,
                loggedUser.clinic
            )
            val visitForm = VisitForm(patientCode = patient.patientCode)
            println("************************************************")
            println("$doctors")
            println("${doctors.size}")
            println("************************************************")
            model.addAttribute("visitForm", visitForm)
            model.addAttribute("doctors", doctors)
            "visits/new"

        } catch (ex: Exception) {
            logger.error("Error loading new visit form: ${ex.message}", ex)
            redirectAttributes.addFlashAttribute("error", "لم يتم العثور على المريض.")
            "redirect:/nurse/dashboard"
        }
    }

    @PostMapping("/save")
    fun saveVisit(
        @ModelAttribute visitForm: VisitForm,
        session: HttpSession,
        redirectAttributes: RedirectAttributes
    ): String {
        return try {
            val nurse = session.getAttribute("loggedUser") as? User
            if (nurse == null || nurse.role != Role.NURSE) {
                redirectAttributes.addFlashAttribute("error", "يرجى تسجيل الدخول كممرضة.")
                return "redirect:/login"
            }

            visitService.createVisit(
                patientCode = visitForm.patientCode,
                visitType = visitForm.visitType,
                complaint = visitForm.complaint,
                doctorUsername = visitForm.doctorPhone,
                nurseName = nurse.fullName,
                status = visitForm.status
            )

            redirectAttributes.addFlashAttribute("success", "✅ تم إضافة الكشف بنجاح.")
            "redirect:/nurse/dashboard"

        }catch (e:IllegalArgumentException){
            redirectAttributes.addFlashAttribute("error", "${e.message}")
            "redirect:new/${visitForm.patientCode}"
        } catch (ex: Exception) {
            logger.error("Error saving visit: ${ex.message}", ex)
            redirectAttributes.addFlashAttribute("error", "⚠️ حدث خطأ أثناء حفظ بيانات الكشف.")
            "redirect:new/${visitForm.patientCode}"
        }
    }

}
