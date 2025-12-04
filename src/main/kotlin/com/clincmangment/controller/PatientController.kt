package com.clincmangment.controller

import com.clincmangment.repository.dto.PatientForm
import com.clincmangment.model.User
import com.clincmangment.service.PatientService
import com.clincmangment.service.UserServiceImpl
import com.clincmangment.utils.Role
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/patients")
class PatientController(
    private val patientService: PatientService,
    private val userService: UserServiceImpl,
    private val httpSession: HttpSession
) {

    private val logger = LoggerFactory.getLogger(PatientController::class.java)

    // صفحة تسجيل مريض جديد
    @GetMapping("/new")
    fun newPatientForm(model: Model): String {
        model.addAttribute("patient", PatientForm())
        return "patients/new"
    }

    // معالجة تسجيل المريض
    @PostMapping("/save")
    fun savePatient(
        @ModelAttribute("patient") form: PatientForm,
        redirectAttributes: RedirectAttributes
    ): String {
        return try {
            val currentClinic = (httpSession.getAttribute("loggedUser") as User).clinic

            // التحقق المسبق من وجود رقم الهاتف ضمن نفس العيادة
            val existingPatient = patientService.findByPhoneAndClinic(form.phone!!, currentClinic)
            if (existingPatient != null) {
                redirectAttributes.addFlashAttribute("error", "رقم الهاتف مستخدم بالفعل في هذه العيادة.")
                return "redirect:/patients/new"
            }

            val user: User = userService.createUser(
                username = form.username,
                rawPassword = form.password,
                role = Role.PATIENT,
                fullName = form.fullName,
                phone = form.phone,
                clinic = currentClinic
            )

            val patientCode = "CLN-${System.currentTimeMillis()}"

            val savedPatient = patientService.createPatient(
                patientCode = patientCode,
                user = user,
                birthDate = form.birthDate,
                notes = form.notes,
                phone = form.phone,
                clinic = currentClinic
            )

            redirectAttributes.addFlashAttribute("success", "تم تسجيل المريض بنجاح ✅")
            return "redirect:/visits/new/${savedPatient.patientCode}"

        } catch (ex: Exception) {
            logger.error("Error saving patient: ${ex.message}")
            redirectAttributes.addFlashAttribute("error", "حدث خطأ أثناء تسجيل المريض. برجاء المحاولة لاحقًا.")
            "redirect:/patients/new"
        }
    }

    // عرض بيانات مريض
    @GetMapping("/view/{patientCode}")
    fun viewPatient(@PathVariable patientCode: String, model: Model): String {
        return try {
            val currentClinic = (httpSession.getAttribute("loggedUser") as User).clinic

            val patient = patientService.findByPatientCodeAndClinic(patientCode, currentClinic)
                ?: throw IllegalArgumentException("Patient not found in this clinic")

            model.addAttribute("patient", patient)
            "patients/view"
        } catch (ex: Exception) {
            logger.error("Error viewing patient: ${ex.message}")
            model.addAttribute("errorMessage", "المريض غير موجود في هذه العيادة أو حدث خطأ في العرض.")
            "error/custom_error"
        }
    }

    // صفحة البحث
    @GetMapping("/search")
    fun searchForm(): String = "patients/search"

    // معالجة البحث عن مريض
    @PostMapping("/search")
    fun searchPatient(
        @RequestParam("keyword") keyword: String,
        model: Model
    ): String {
        return try {
            val currentClinic = (httpSession.getAttribute("loggedUser") as User).clinic

            val patient = patientService.findByCodeOrPhoneAndClinic(keyword, currentClinic)
            if (patient != null) {
                model.addAttribute("patient", patient)
                "patients/found"
            } else {
                model.addAttribute("notFound", true)
                "patients/search"
            }
        } catch (ex: Exception) {
            logger.error("Error searching patient: ${ex.message}")
            model.addAttribute("errorMessage", "حدث خطأ أثناء البحث عن المريض.")
            "error/custom_error"
        }
    }
}
