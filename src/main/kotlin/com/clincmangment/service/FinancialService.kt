package com.clincmangment.service

import com.clincmangment.repository.ClincRepository
import com.clincmangment.repository.ExpenseRepository
import com.clincmangment.repository.VisitRepository
import com.clincmangment.repository.dto.*
import com.clincmangment.repository.PrescriptionRepository
import com.clincmangment.repository.dto.FinancialSummaryDTO
import com.clincmangment.utils.VisitType
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import kotlin.math.round

@Service
class FinancialService(
    private val visitRepository: VisitRepository,
    private val expenseRepository: ExpenseRepository,
    private val clinicRepository: ClincRepository,
    private val prescriptionRepository: PrescriptionRepository
) {

    private fun sumRevenueForPeriod(clinicId: Long, start: LocalDateTime, end: LocalDateTime): Double {
        val clinic = clinicRepository.findById(clinicId).orElseThrow()
        val visits = visitRepository.findVisitsForRevenue(clinicId, start, end)
        val sum = visits.sumOf { v -> v.visitPrice}
        return round(sum * 100) / 100.0
    }

    fun getSummaryForPeriod(clinicId: Long, start: LocalDateTime, end: LocalDateTime): FinancialSummaryDTO {
        val revenuePeriod = sumRevenueForPeriod(clinicId, start, end)
        val expensePeriod = expenseRepository.sumExpensesByClinicAndPeriod(clinicId, start, end)
        val profit = round((revenuePeriod - expensePeriod) * 100) / 100.0

        // This month
        val now = LocalDate.now()
        val startOfMonth = YearMonth.now().atDay(1).atStartOfDay()
        val endOfMonth = startOfMonth.plusMonths(1)
        val revenueThisMonth = sumRevenueForPeriod(clinicId, startOfMonth, endOfMonth)
        val expenseThisMonth = expenseRepository.sumExpensesByClinicAndPeriod(clinicId, startOfMonth, endOfMonth)
        val profitThisMonth = round((revenueThisMonth - expenseThisMonth) * 100) / 100.0

        // last 12 months monthly totals
        val revenueLast12 = mutableListOf<Double>()
        val expensesLast12 = mutableListOf<Double>()
        val currentMonth = YearMonth.now()
        for (i in 11 downTo 0) {
            val ym = currentMonth.minusMonths(i.toLong())
            val s = ym.atDay(1).atStartOfDay()
            val e = ym.plusMonths(1).atDay(1).atStartOfDay()
            revenueLast12.add(sumRevenueForPeriod(clinicId, s, e))
            expensesLast12.add(expenseRepository.sumExpensesByClinicAndPeriod(clinicId, s, e))
        }

        // revenue vs expense last 30 days (daily)
        val rv30 = mutableListOf<DailySeries>()
        val today = LocalDate.now()
        for (i in 29 downTo 0) {
            val day = today.minusDays(i.toLong())
            val s = day.atStartOfDay()
            val e = s.plusDays(1)
            val r = sumRevenueForPeriod(clinicId, s, e)
            val ex = expenseRepository.sumExpensesByClinicAndPeriod(clinicId, s, e)
            rv30.add(DailySeries(day.toString(), r, ex))
        }

        // expense breakdown (group by title, last 90 days)
        val since = LocalDate.now().minusDays(90).atStartOfDay()
        val until = LocalDate.now().plusDays(1).atStartOfDay()
        val expList = expenseRepository.findByClinicIdAndCreatedAtBetween(clinicId, since, until)
        val breakdown = expList.groupingBy { it.title }.fold(0.0) { acc, e -> acc + e.amount }

        return FinancialSummaryDTO(
            revenuePeriod = revenuePeriod,
            expensePeriod = expensePeriod,
            profit = profit,
            revenueThisMonth = revenueThisMonth,
            expenseThisMonth = expenseThisMonth,
            profitThisMonth = profitThisMonth,
            revenueLast12Months = revenueLast12,
            expensesLast12Months = expensesLast12,
            revenueVsExpenseLast30Days = rv30,
            expenseBreakdown = breakdown
        )
    }

    fun getDashboardFinancials(clinicId: Long): Map<String, Any> {
        val today = LocalDate.now().atStartOfDay()
        val tomorrow = today.plusDays(1)
        val revenueToday = sumRevenueForPeriod(clinicId, today, tomorrow)
        val expenseToday = expenseRepository.sumExpensesByClinicAndPeriod(clinicId, today, tomorrow)
        val profitToday = round((revenueToday - expenseToday) * 100) / 100.0
        return mapOf("revenueToday" to revenueToday, "expenseToday" to expenseToday, "profitToday" to profitToday)
    }
}
