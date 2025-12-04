package com.clincmangment.controller

import com.clincmangment.repository.ClincRepository
import com.clincmangment.model.User
import com.clincmangment.service.DashboardService
import com.clincmangment.utils.SubscriptionType
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/clinc")
class ClincController(
    private val dashboardService: DashboardService,
    private val clinicRepository: ClincRepository,
    private val httpSession: HttpSession
) {

    @GetMapping("/dashboard")
    fun clinicDashboard( model: Model): String {
        val clinicId =  httpSession.getAttribute("loggedUser") as? User ?: return "redirect:/login"
        val clinic = clinicRepository.findById(clinicId.clinic.id!!).orElseThrow()
        if (clinic.subscriptionType != SubscriptionType.ADVANCED) {
            // لو مش مشترك في المتقدم، نعرض صفحة upsell أو نرجع رسالة
            model.addAttribute("clinic", clinic)
            return "clinc/upgrade" // صفحة بسيطة تعرض الفرق و CTA للاشتراك
        }
        val dto = dashboardService.getDashboard(clinicId.clinic.id!!)
        model.addAttribute("clinic", clinic)
        model.addAttribute("dashboard", dto)
        return "clinc/dashboard"
    }
}
