package com.clincmangment.repository

import com.clincmangment.repository.model.PrescriptionTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.clincmangment.repository.model.User


@Repository
interface PrescriptionTemplateRepository : JpaRepository<PrescriptionTemplate, Long> {
    fun findAllByDoctor(doctor: User): List<PrescriptionTemplate>
    fun findByIdAndDoctor(id: Long, doctor: User): PrescriptionTemplate
}