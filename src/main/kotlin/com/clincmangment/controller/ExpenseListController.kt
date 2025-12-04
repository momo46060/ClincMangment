package com.clincmangment.controller

import com.clincmangment.repository.ExpenseRepository
import com.clincmangment.repository.ClincRepository
import com.clincmangment.model.User
import com.clincmangment.utils.getCurrentUser
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@Controller
@RequestMapping("/clinics/expenses")
class ExpenseListController(
    private val expenseRepository: ExpenseRepository,
    private val clinicRepository: ClincRepository,
    private val httpSession: HttpSession
) {

    @GetMapping
    fun listExpenses(
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
        model: Model
    ): String {
        val currentUser = getCurrentUser(httpSession) as User

        val clinic = clinicRepository.findById(currentUser.clinic.id!!).orElseThrow()
        val start: LocalDateTime = if (!startDate.isNullOrEmpty()) LocalDate.parse(startDate).atStartOfDay() else LocalDate.now().minusMonths(1).atStartOfDay()
        val end: LocalDateTime = if (!endDate.isNullOrEmpty()) LocalDate.parse(endDate).atTime(23,59,59) else LocalDateTime.now()

        val expenses = expenseRepository.findByClinicIdAndCreatedAtBetween(currentUser.clinic.id!!, start, end)

        model.addAttribute("clinic", clinic)
        model.addAttribute("expenses", expenses)
        model.addAttribute("startDate", start.toLocalDate())
        model.addAttribute("endDate", end.toLocalDate())
        return "expenses/expense-list"
    }
}
