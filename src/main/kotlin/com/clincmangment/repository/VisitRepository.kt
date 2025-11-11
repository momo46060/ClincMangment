package com.clincmangment.repository

import com.clincmangment.repository.model.Visit
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Repository
interface VisitRepository : JpaRepository<Visit, Long> {
    @Query("SELECT v FROM Visit v" +
     " WHERE v.doctor.id = :doctorId AND v.visitDate BETWEEN :startOfDay AND :endOfDay AND v.clinic.id = :clinicId")
    fun findVisitsByDoctorAndDate(
        @Param("doctorId") doctorId: Long,
        @Param("startOfDay") startOfDay: LocalDateTime,
        @Param("endOfDay") endOfDay: LocalDateTime,
        @Param("clinicId") clinicId: Long
    ): List<Visit>
    fun findByDoctorIdAndStatus(doctorId: Long, status: String): List<Visit>
    fun findByDoctorIdAndClinicId(doctorId: Long, clinicId: Long): List<Visit>
    fun findByClinicIdAndVisitDate(clinicId: Long, date: LocalDate): List<Visit>
    @Query("SELECT v FROM Visit v WHERE v.visitDate >= :start AND v.visitDate < :end")
    fun findByVisitDate(@Param("start") start: LocalDateTime, @Param("end") end: LocalDateTime): List<Visit>
    @Query("SELECT v FROM Visit v WHERE DATE(v.visitDate) = DATE(:date)")
    fun findByVisitDate(@Param("date") date: LocalDateTime): List<Visit>
    fun findAllByPatient_IdOrderByVisitDateDesc(patientId: Long): List<Visit>
    fun findByPatient_IdAndId(patientId: Long, visitId: Long): Optional<Visit>
    fun findAllByDoctor_IdOrderByVisitDateDesc(doctorId: Long): List<Visit>
    fun findAllByScheduledConsultationAfter(orderDate: java.time.LocalDateTime): List<Visit>
    fun findAllByPatient_Id(patientId: Long, pageable: Pageable): Page<Visit>
    fun findByClinicId(clinicId: Long): List<Visit>
    fun findByClinicIdAndVisitDateBetween(clinicId: Long, start: LocalDateTime, end: LocalDateTime): List<Visit>
    fun findByDoctorIdAndStatusAndClinicId(doctorId: Long, status: String, clinicId: Long): List<Visit>
    fun findAllByPatient_IdAndClinic_IdOrderByVisitDateDesc(patientId: Long, clinicId: Long): List<Visit>
    fun findAllByDoctor_IdAndClinic_IdOrderByVisitDateDesc(doctorId: Long, clinicId: Long): List<Visit>

}
