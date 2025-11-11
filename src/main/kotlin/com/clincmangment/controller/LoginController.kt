package com.clincmangment.controller

import com.clincmangment.repository.dto.LoginForm
import com.clincmangment.repository.model.User
import com.clincmangment.service.UserServiceImpl
import com.clincmangment.utils.Role
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class LoginController(
    private val userService: UserServiceImpl
) {

    @GetMapping("/login")
    fun loginPage(
        model: Model,
        @RequestParam(required = false) error: String?,
        session: HttpSession,
        request: HttpServletRequest
    ): String {

        model.addAttribute("loginForm", LoginForm())
        if (error != null) {
            model.addAttribute("error", "Invalid username or password")
        }
        return "auth/login"
    }

    @PostMapping("/login")
    fun loginSubmit(
        @ModelAttribute loginForm: LoginForm,
        session: HttpSession,
        redirectAttributes: RedirectAttributes
    ): String  {
        val user = userService.login(loginForm.phone, loginForm.password)
        return if (user != null) {
            session.setAttribute("loggedUser", user)
            when (user.role) {
                Role.DOCTOR -> "redirect:/doctor/dashboard"
                Role.NURSE -> "redirect:/nurse/dashboard"
                Role.PATIENT -> "redirect:/patients/view/${user.phone}"
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "رقم الموبايل أو كلمة المرور غير صحيحة")
            "redirect:/login"
        }
    }
    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        println("=== LOGOUT ===")
        session.invalidate()
        return "redirect:/login"
    }
}