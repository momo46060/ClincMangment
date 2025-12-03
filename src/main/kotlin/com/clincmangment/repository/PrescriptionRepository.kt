package com.clincmangment.repository

import com.clincmangment.repository.model.Prescription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PrescriptionRepository : JpaRepository<Prescription, Long> {
    fun findAllByVisit_Id(visitId: Long): List<Prescription>
    fun findByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<Prescription>
    fun findByVisitClinicIdAndCreatedAtBetween(clinicId: Long, start: LocalDateTime, end: LocalDateTime): List<Prescription>
    fun findAllByVisitId(visitId: Long): List<Prescription>

}