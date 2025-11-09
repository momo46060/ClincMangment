package com.clincmangment.repository

import com.clincmangment.repository.model.Prescription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PrescriptionRepository : JpaRepository<Prescription, Long> {
    fun findAllByVisit_Id(visitId: Long): List<Prescription>
}