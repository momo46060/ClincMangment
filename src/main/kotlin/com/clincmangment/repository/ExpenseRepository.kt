package com.clincmangment.repository

import com.clincmangment.model.Expense
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface ExpenseRepository : JpaRepository<Expense, Long> {

    fun findByClinicIdAndCreatedAtBetween(clinicId: Long, start: LocalDateTime, end: LocalDateTime): List<Expense>

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.clinic.id = :clinicId AND e.createdAt BETWEEN :start AND :end")
    fun sumExpensesByClinicAndPeriod(
        @Param("clinicId") clinicId: Long,
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime
    ): Double
}
