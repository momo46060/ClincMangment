package com.clincmangment.controller

import com.clincmangment.model.User
import com.clincmangment.service.ClinicService
import com.clincmangment.service.UserServiceImpl
import com.clincmangment.service.VisitService
import com.clincmangment.utils.Role
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/nurse")
class NurseController(
    private val visitService: VisitService,
    private val userService: UserServiceImpl,
    private val clinicService: ClinicService,
) {

    private val logger = LoggerFactory.getLogger(NurseController::class.java)
    @GetMapping("/dashboard")
    fun nurseDashboard(model: Model, session: HttpSession): String {
        val nurse = session.getAttribute("loggedUser") as? User ?: return "redirect:/login"

        val subscriptionActive = clinicService.isSubscriptionActive(nurse.clinic)

        model.addAttribute("subscriptionExpired", !subscriptionActive)
        val doctors = userService.getUsersByRoleAndClinic(Role.DOCTOR, nurse.clinic)

        val todayVisits = visitService.getTodayVisitsByClinic(nurse.clinic.id!!).sortedBy { it.id }
        model.addAttribute("doctors", doctors)
        model.addAttribute("todayVisits", todayVisits)
        model.addAttribute("nurse", nurse)
        return "nurse/dashboard"
    }


    @PostMapping("/visit/{id}/status")
    fun updateVisitStatus(
        @PathVariable id: Long,
        @RequestParam status: String,
        model: Model
    ): String {
        return try {
            visitService.updateVisitStatus(id, status)
            "redirect:/nurse/dashboard"
        } catch (ex: Exception) {
            logger.error("Error updating visit status: ${ex.message}")
            model.addAttribute("errorMessage", "حدث خطأ أثناء تحديث حالة الزيارة.")
            "error/custom_error"
        }
    }

    @PostMapping("/visit/{id}/cancel")
    fun cancelVisit(
        @PathVariable id: Long,
        model: Model
    ): String {
        return try {
            visitService.cancelVisit(id)
            "redirect:/nurse/dashboard"
        } catch (ex: Exception) {
            logger.error("Error canceling visit: ${ex.message}")
            model.addAttribute("errorMessage", "حدث خطأ أثناء إلغاء الزيارة.")
            "error/custom_error"
        }
    }
}
