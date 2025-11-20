package com.clincmangment.controller

import com.clincmangment.repository.ClincRepository
import com.clincmangment.repository.ExpenseRepository
import com.clincmangment.repository.UserRepository
import com.clincmangment.repository.model.Expense
import com.clincmangment.repository.model.User
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/expenses")
class ExpenseController(
    private val expenseRepository: ExpenseRepository,
    private val clinicRepository: ClincRepository,
    private val userRepository: UserRepository,
) {

    @GetMapping("/add")
    fun addExpenseForm(): String {
        return "expenses/add-expense"
    }

    @PostMapping("/save")
    fun saveExpense(
        @RequestParam title: String,
        @RequestParam amount: Double,
        @RequestParam(required = false) notes: String?, httpSession: HttpSession
    ): String {

        val user =  httpSession.getAttribute("loggedUser") as User

        val clinic = user.clinic

        val expense = Expense(
            title = title,
            amount = amount,
            notes = notes,
            clinic = clinic,
            createdBy = user
        )

        expenseRepository.save(expense)

        return "redirect:/clinics/${clinic.id}/dashboard"
    }
}
