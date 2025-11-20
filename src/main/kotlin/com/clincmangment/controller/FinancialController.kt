package com.clincmangment.controller

import com.clincmangment.service.FinancialService
import com.clincmangment.repository.ClincRepository
import com.clincmangment.repository.ExpenseRepository
import com.clincmangment.repository.UserRepository
import com.clincmangment.repository.model.Expense
import com.clincmangment.repository.model.User
import com.clincmangment.utils.getCurrentUser
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@Controller
@RequestMapping("/clinics")
class FinancialController(
    private val financialService: FinancialService,
    private val clinicRepository: ClincRepository,
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository,
    private val httpSession: HttpSession
) {

    @GetMapping("/financial")
fun financialPage(
    @RequestParam(required = false) from: String?,
    @RequestParam(required = false) to: String?,
    model: Model
): String {
    val currentUser = getCurrentUser(httpSession) as User
    val clinic = clinicRepository.findById(currentUser.clinic.id!!).orElseThrow()

    val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // تحديد التواريخ الافتراضية
    val defaultFrom = LocalDate.now().minusMonths(1)
    val defaultTo = LocalDate.now()

    val periodStart = from?.let { LocalDate.parse(it, formatter).atStartOfDay() } ?: defaultFrom.atStartOfDay()
    val periodEnd = to?.let { LocalDate.parse(it, formatter).atTime(23, 59, 59) } ?: defaultTo.atTime(23, 59, 59)

    val summary = financialService.getSummaryForPeriod(currentUser.clinic.id!!, periodStart, periodEnd)

    model.addAttribute("clinic", clinic)
    model.addAttribute("summary", summary)
    // تحويل التواريخ للصيغة المطلوبة yyyy-MM-dd
    model.addAttribute("fromDate", from ?: defaultFrom.format(formatter))
    model.addAttribute("toDate", to ?: defaultTo.format(formatter))

    return "financial/financial-report"
}

    // Add expense form
    @GetMapping("/expenses/add")
    fun addExpenseForm( model: Model): String {
        val currentUser = getCurrentUser(httpSession) as User

        model.addAttribute("clinicId", currentUser.clinic.id!!)
        return "financial/add-expense"
    }

    @PostMapping("/expenses/save")
    fun saveExpense(
        @RequestParam title: String,
        @RequestParam amount: Double,
        @RequestParam(required = false) notes: String?
    ): String {
        // get logged user (فرضًا لديك SecurityContext)
        val user = getCurrentUser(httpSession) as User
        // <-- استبدل هذه الدالة بكيفية جلب المستخدم في مشروعك
        val clinic = clinicRepository.findById(user.clinic.id!!).orElseThrow()
        val exp = Expense(title = title, amount = amount, notes = notes, clinic = clinic, createdBy = user)
        expenseRepository.save(exp)
        return "redirect:/clinics/financial"
    }


}
