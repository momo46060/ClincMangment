package com.clincmangment.repository.dto


data class SimpleSeries(val label: String, val value: Double)
data class DailySeries(val dateLabel: String, val revenue: Double, val expense: Double)
data class FinancialSummaryDTO(
    val revenuePeriod: Double,
    val expensePeriod: Double,
    val profit: Double,
    val revenueThisMonth: Double,
    val expenseThisMonth: Double,
    val profitThisMonth: Double,
    val revenueLast12Months: List<Double>, // monthly totals
    val expensesLast12Months: List<Double>,
    val revenueVsExpenseLast30Days: List<DailySeries>,
    val expenseBreakdown: Map<String, Double> // title -> sum
)
