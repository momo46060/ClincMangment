package com.clincmangment.controller

import com.clincmangment.model.User
import com.clincmangment.service.ClinicServiceService
import jakarta.servlet.http.HttpSession
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/doctor/services")
class ClinicServiceController(
    val clinicServiceService: ClinicServiceService
) {

    @GetMapping
    fun listServices(model: Model, session: HttpSession): String {
        val loggedUser = session.getAttribute("loggedUser") as User

        model.addAttribute("services",
            clinicServiceService.getDoctorServices(loggedUser.id!!))
        return "doctor/services-list"
    }

    @PostMapping("/add")
    fun addService(
        @RequestParam name: String,
        @RequestParam price: Double,
        session: HttpSession
    ): String {
        val loggedUser = session.getAttribute("loggedUser") as User

        clinicServiceService.addService(loggedUser.id!!, name, price)
        return "redirect:/doctor/services"
    }

    @PostMapping("/delete")
    fun deleteService(@RequestParam id: Long): String {
        clinicServiceService.deleteService(id)
        return "redirect:/doctor/services"
    }
}
