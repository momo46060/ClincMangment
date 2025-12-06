package com.clincmangment.controller

import com.clincmangment.model.Clinic
import com.clincmangment.service.ClinicService
import com.clincmangment.service.UserServiceImpl
import com.clincmangment.utils.Role
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate

@Controller
@RequestMapping("/admin")
class AdminController(
    private val clinicService: ClinicService,
    private val doctorService: UserServiceImpl
) {

    @GetMapping("/manage")
    fun managePage(model: Model): String {
        model.addAttribute("clinics", clinicService.getAllClinics().filter {
            it.name != "test"
        })
        model.addAttribute("doctors", doctorService.findAllByDoctors(Role.DOCTOR))
        return "admin/dashboard"
    }

    // إضافة عيادة
    @PostMapping("/clinic/add")
    fun addClinic(
        @RequestParam name: String,
        @RequestParam address: String,
        @RequestParam subscriptionEndDate: LocalDate,
        @RequestParam phone: String,
        redirectAttributes: RedirectAttributes,
        ): String {
        return try {
            clinicService.createClinic(
                Clinic(
                    name = name,
                    address = address,
                    subscriptionEndDate = subscriptionEndDate,
                    phone = phone
                )
            )
            redirectAttributes.addFlashAttribute("success", "تمت اضافه العياده بنجاح")
            "redirect:/admin/manage"
        } catch (e: Exception) {
            e.printStackTrace()
            redirectAttributes.addFlashAttribute("error", "${e.message}")
            "redirect:/admin/manage"
        }
    }

    // إضافة دكتور
    @PostMapping("/doctor/add")
    fun addDoctor(
        @RequestParam fullName: String,
        @RequestParam email: String,
        @RequestParam phone: String,
        @RequestParam password: String,
        @RequestParam clinicId: Long,
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
            doctorService.createUser(
                username = fullName,
                rawPassword = password,
                role = Role.DOCTOR,
                fullName = "Dr.$fullName",
                phone = phone,
                email = email,
                clinic = clinicService.findClinicById(clinicId)
            )
            redirectAttributes.addFlashAttribute("success", "تمت إضافة الدكتور بنجاح")
            return "redirect:/admin/manage"

        } catch (e: Exception) {
            e.printStackTrace()
            redirectAttributes.addFlashAttribute("error", "رقم الهاتف موجود مسيقا")
            return "redirect:/admin/manage"
        }
    }
}
