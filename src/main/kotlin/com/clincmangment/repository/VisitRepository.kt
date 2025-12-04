package com.clincmangment.repository

import com.clincmangment.model.Visit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface VisitRepository : JpaRepository<Visit, Long> {
    fun countByClinicIdAndVisitDateBetween(clinicId: Long, start: LocalDateTime, end: LocalDateTime): Long
    fun countByPatientId(patientId: Long): Long
    fun findTopByPatientIdOrderByVisitDateDesc(patientId: Long): Visit?


    @Query(
        "SELECT v FROM Visit v" +
                " WHERE v.doctor.id = :doctorId AND v.visitDate" +
                " BETWEEN :startOfDay AND :endOfDay AND v.clinic.id = :clinicId"
    )
    fun findVisitsByDoctorAndDate(
        @Param("doctorId") doctorId: Long,
        @Param("startOfDay") startOfDay: LocalDateTime,
        @Param("endOfDay") endOfDay: LocalDateTime,
        @Param("clinicId") clinicId: Long
    ): List<Visit>

    fun findByClinicId(clinicId: Long): List<Visit>
    fun findByClinicIdAndVisitDateBetween(clinicId: Long, start: LocalDateTime, end: LocalDateTime): List<Visit>
    fun findByDoctorIdAndStatusAndClinicId(doctorId: Long, status: String, clinicId: Long): List<Visit>
    fun findAllByPatient_IdAndClinic_IdOrderByVisitDateDesc(patientId: Long, clinicId: Long): List<Visit>
    fun findAllByDoctor_IdAndClinic_IdAndVisitDateBetweenOrderByVisitDateDesc(
        doctorId: Long,
        clinicId: Long,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): List<Visit>



    fun findByClinicIdAndVisitDateBetweenOrderByVisitDateAsc(clinicId: Long, start: LocalDateTime, end: LocalDateTime): List<Visit>

    @Query("SELECT v FROM Visit v WHERE v.clinic.id = :clinicId AND v.visitDate > :now ORDER BY v.visitDate ASC")
    fun findNextAppointments(@Param("clinicId") clinicId: Long, @Param("now") now: LocalDateTime): List<Visit>

    // visits of a range (for weekly revenue etc.)

    // simple sum revenue using clinic's prices per visitType
    @Query("SELECT v FROM Visit v WHERE v.clinic.id = :clinicId AND v.visitDate BETWEEN :start AND :end")
    fun findVisitsForRevenue(@Param("clinicId") clinicId: Long, @Param("start") start: LocalDateTime, @Param("end") end: LocalDateTime): List<Visit>

    // count by status for today
    fun countByClinicIdAndStatusAndVisitDateBetween(clinicId: Long, status: String, start: LocalDateTime, end: LocalDateTime): Long

    // visits per doctor today
    @Query("SELECT v.doctor.id, v.doctor.fullName, COUNT(v) FROM Visit v WHERE v.clinic.id = :clinicId AND v.visitDate BETWEEN :start AND :end GROUP BY v.doctor.id, v.doctor.fullName")
    fun countVisitsPerDoctor(@Param("clinicId") clinicId: Long, @Param("start") start: LocalDateTime, @Param("end") end: LocalDateTime): List<Array<Any>>


    // لحساب قائمة الزيارات لفترة (نستخدمها لحساب الإيرادات في Service)
    fun findVisitsByClinicIdAndVisitDateBetween(clinicId: Long, start: LocalDateTime, end: LocalDateTime): List<Visit>



}
