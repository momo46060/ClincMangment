package com.clincmangment.repository

import com.clincmangment.repository.model.Clinic
import com.clincmangment.repository.model.Patient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional

@Repository
interface PatientRepository : JpaRepository<Patient, Long> {
    fun countByClinicIdAndCreatedAtBetween(clinicId: Long, start: LocalDateTime, end: LocalDateTime): Long

    fun findByClinicIdAndCreatedAtBetween(clinicId: Long, start: LocalDateTime, end: LocalDateTime): List<Patient>

    // البحث بواسطة كود المريض مع مراعاة العيادة
    fun findByPatientCodeAndClinic(patientCode: String, clinic: Clinic): Patient?

    // البحث بواسطة رقم الهاتف مع مراعاة العيادة
    fun findByPhoneAndClinic(phone: String, clinic: Clinic): Patient?

    fun findByPatientCode(code: String): Optional<Patient>
    fun findByPhone(phone: String): Optional<Patient>
    fun findByUser_Id(userId: Long): Optional<Patient>
    fun findByPatientCodeContainingIgnoreCaseOrPhoneContaining(patientCode: String, phone: String): List<Patient>

    // البحث بالاسم
    @Query("SELECT p FROM Patient p WHERE LOWER(p.user.fullName) LIKE LOWER(:name)")
    fun searchByName(@Param("name") name: String, pageable: Pageable): Page<Patient>

    // كل المرضى في عيادة معينة
    fun findAllByClinic(clinic: Clinic): List<Patient>
}
