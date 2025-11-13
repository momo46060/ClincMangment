package com.clincmangment.service

import com.clincmangment.repository.VisitRepository
import com.clincmangment.repository.model.Patient
import com.clincmangment.repository.model.User
import com.clincmangment.repository.model.Visit
import com.clincmangment.utils.VisitType
import jakarta.transaction.Transactional
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Optional

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    private val patientService: PatientService,
    private val userService: UserServiceImpl
) {
   fun findVisitsByDoctorAndDate(
     doctorId: Long,
    startOfDay: LocalDateTime,
    endOfDay: LocalDateTime,
    clinicId: Long
    ): List<Visit> = visitRepository.findVisitsByDoctorAndDate(doctorId, startOfDay, endOfDay,clinicId)
    fun save(visit: Visit): Visit = visitRepository.save(visit)

    // جلب جميع الزيارات الخاصة بالعيادة
    fun getVisitsByClinic(clinicId: Long): List<Visit> = visitRepository.findByClinicId(clinicId)

    // جلب زيارات اليوم لكل العيادة
    fun getTodayVisitsByClinic(clinicId: Long): List<Visit> {
        val startOfDay = LocalDate.now().atStartOfDay()
        val endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
        return visitRepository.findByClinicIdAndVisitDateBetween(clinicId, startOfDay, endOfDay)
    }

    fun getVisitsByDoctorAndStatus(doctorId: Long, status: String, clinicId: Long): List<Visit> {
        return visitRepository.findByDoctorIdAndStatusAndClinicId(doctorId, status, clinicId)
    }

    fun createVisit(
        patientCode: String,
        visitType: String,
        complaint: String?,
        doctorUsername: String?,
        nurseName: String,
        status: String
    ) {
        val patient = patientService.findByPatientCode(patientCode)
            .orElseThrow { IllegalArgumentException("Patient not found") }

        val doctor = doctorUsername?.let { userService.findByPhone(it).orElse(null) }

        val visit = Visit(
            patient = patient,
            visitType = visitType.let { VisitType.valueOf(it) },
            complaint = complaint,
            doctor = doctor,
            nurseName = nurseName,
            visitDate = LocalDateTime.now(),
            status = status,
            clinic = patient.clinic // ربط الزيارة بالعيادة
        )

        visitRepository.save(visit)
    }

    @Transactional
    fun updateVisitStatus(visitId: Long, newStatus: String) {
        val visit = visitRepository.findById(visitId)
            .orElseThrow { IllegalArgumentException("Visit not found") }

        visit.status = newStatus
        visitRepository.save(visit)
    }

    @Transactional
    fun cancelVisit(visitId: Long) {
        val visit = visitRepository.findById(visitId)
            .orElseThrow { IllegalArgumentException("Visit not found") }

        visit.status = "ملغاة"
        visitRepository.save(visit)
    }

    fun getVisitsByPatient(patientId: Long, clinicId: Long): List<Visit> =
        visitRepository.findAllByPatient_IdAndClinic_IdOrderByVisitDateDesc(patientId, clinicId)

    fun getVisitsByDoctor(doctorId: Long, clinicId: Long): List<Visit> {
        val startOfDay = LocalDate.now().atStartOfDay()
        val endOfDay = startOfDay.plusDays(1).minusNanos(1)
         val list=visitRepository.findAllByDoctor_IdAndClinic_IdAndVisitDateBetweenOrderByVisitDateDesc(
            doctorId,
            clinicId,
            startOfDay,
            endOfDay
        )
        println(list)

        return list
    }
    fun getVisitById(id: Long): Optional<Visit> = visitRepository.findById(id)
}
