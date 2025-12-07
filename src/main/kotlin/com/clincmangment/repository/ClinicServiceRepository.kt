package com.clincmangment.repository

import com.clincmangment.model.ClinicService
import org.springframework.data.jpa.repository.JpaRepository

interface ClinicServiceRepository : JpaRepository<ClinicService, Long> {
    fun findByDoctorId(doctorId: Long): List<ClinicService>

}
