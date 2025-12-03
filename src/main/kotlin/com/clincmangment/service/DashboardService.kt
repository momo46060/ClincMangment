package com.clincmangment.service


// DashboardService.kt

import com.clincmangment.repository.ClincRepository
import com.clincmangment.repository.PatientRepository
import com.clincmangment.repository.PrescriptionRepository
import com.clincmangment.repository.VisitRepository
import com.clincmangment.repository.dto.AppointmentShortDTO
import com.clincmangment.repository.dto.DashboardDTO
import com.clincmangment.repository.dto.NameCountDTO
import com.clincmangment.repository.dto.PatientVisitsDTO
import com.clincmangment.repository.dto.SeriesItemDTO
import com.clincmangment.repository.model.Visit
import com.clincmangment.repository.model.Prescription
import com.clincmangment.utils.VisitType
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round

@Service
class DashboardService(
    private val visitRepository: VisitRepository,
    private val patientRepository: PatientRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val clinicRepository: ClincRepository
) {

    fun getDashboard(clinicId: Long): DashboardDTO {
        val today = LocalDate.now()
        val startOfDay = today.atStartOfDay()
        val endOfDay = startOfDay.plusDays(1)

        val visitsToday = visitRepository.countByClinicIdAndVisitDateBetween(clinicId, startOfDay, endOfDay)
        val waiting = visitRepository.countByClinicIdAndStatusAndVisitDateBetween(clinicId, "في الانتظار", startOfDay, endOfDay)
        val completed = visitRepository.countByClinicIdAndStatusAndVisitDateBetween(clinicId, "تم", startOfDay, endOfDay)
        val canceled = visitRepository.countByClinicIdAndStatusAndVisitDateBetween(clinicId, "ملغاة", startOfDay, endOfDay)

        val newPatients = patientRepository.countByClinicIdAndCreatedAtBetween(clinicId, startOfDay, endOfDay)

        // Revenue today: sum by visitType and clinic prices
        val visitsForRevenue = visitRepository.findVisitsForRevenue(clinicId, startOfDay, endOfDay)
        val clinic = clinicRepository.findById(clinicId).orElseThrow()
        println("**************************")
        println("${visitsForRevenue}")
        println("**************************")
        val revenueToday = visitsForRevenue.sumOf { v ->
            when (v.visitType) {
                VisitType.CHECKUP -> {
                    println("**************************")
                    println("${clinic.checkUpPrice}")
                    println("**************************")
                    clinic.checkUpPrice

                }
                else -> {
                    println("**************************")
                    println("${clinic.followUpPrice}")
                    println("**************************")
                    clinic.followUpPrice
                }
            }
        }

        // Next appointments (take next 10)
        val nextAppointmentsRaw = visitRepository.findNextAppointments(clinicId, LocalDateTime.now()).take(10)
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val nextAppointments = nextAppointmentsRaw.map { v ->
            AppointmentShortDTO(
                visitId = v.id ?: 0,
                patientName = v.patient!!.user.fullName,
                doctorName = v.doctor?.fullName,
                visitType = v.visitType!!.name,
                visitTime = v.visitDate.format(fmt)
            )
        }

        // Visits per doctor today
        val perDoctorRaw = visitRepository.countVisitsPerDoctor(clinicId, startOfDay, endOfDay)
        val visitsPerDoctor = perDoctorRaw.map { arr ->
            val name = arr[1]?.toString() ?: "غير محدد"
            val count = (arr[2] as Number).toLong()
            SeriesItemDTO(name, count)
        }

        // Weekly revenue (last 7 days)
        val weeklyRevenue = mutableListOf<Double>()
        for (i in 6 downTo 0) {
            val day = today.minusDays(i.toLong())
            val s = day.atStartOfDay()
            val e = s.plusDays(1)
            val vs = visitRepository.findVisitsForRevenue(clinicId, s, e)
            val sum = vs.sumOf { v ->
                when (v.visitType) {
                    VisitType.CONSULTATION -> clinic.checkUpPrice
                    else -> clinic.followUpPrice
                }
            }
            weeklyRevenue.add(round(sum * 100) / 100.0)
        }

        // Visit type breakdown (today)
        val visitsTodayList = visitRepository.findByClinicIdAndVisitDateBetween(clinicId, startOfDay, endOfDay)
        val typeBreakdown = visitsTodayList.groupingBy { it.visitType!!.name }.eachCount().mapValues { it.value.toLong() }

        // Average waiting time (we'll approximate using createdAt to visitDate if createdAt exists)
        val waitingMinutes = visitsTodayList.mapNotNull { v ->
            val created = v.createdAt ?: return@mapNotNull null
            val diff = java.time.Duration.between(created, v.visitDate).toMinutes()
            if (diff >= 0) diff else null
        }
        val avgWaiting = if (waitingMinutes.isNotEmpty()) waitingMinutes.average() else 0.0

        // Top 5 patients by visits (all time or last month — we'll do last 90 days)
        val since = today.minusDays(90).atStartOfDay()
        val until = today.atStartOfDay().plusDays(1)
        val visits90 = visitRepository.findByClinicIdAndVisitDateBetween(clinicId, since, until)
        val topPatients = visits90.groupingBy { it.patient!!.user.fullName }.eachCount()
            .entries.sortedByDescending { it.value }.take(5).map { PatientVisitsDTO(it.key, it.value.toLong()) }

        // Top medicines last 30 days (parse prescription.content heuristically)
        val since30 = today.minusDays(30).atStartOfDay()
        val prescriptions30 = prescriptionRepository.findByVisitClinicIdAndCreatedAtBetween(clinicId, since30, endOfDay)
        val medicineCounts = mutableMapOf<String, Long>()
        prescriptions30.forEach { p ->
            extractMedicineNames(p).forEach { med ->
                medicineCounts[med] = (medicineCounts[med] ?: 0L) + 1
            }
        }
        val topMedicines = medicineCounts.entries.sortedByDescending { it.value }.take(5).map { NameCountDTO(it.key, it.value) }

        // Alerts
        val alerts = mutableListOf<String>()
        // visits without diagnosis today
        val withoutDiagnosis = visitsTodayList.filter { it.diagnosis.isNullOrBlank() }
        if (withoutDiagnosis.isNotEmpty()) alerts.add("يوجد ${withoutDiagnosis.size} زيارات اليوم بدون تشخيص")
        // scheduled consultations in next 48 hours
        val scheduledSoon = visitRepository.findNextAppointments(clinicId, LocalDateTime.now()).filter { v ->
            v.scheduledConsultation != null && java.time.Duration.between(LocalDateTime.now(), v.scheduledConsultation).toHours() <= 48
        }
        if (scheduledSoon.isNotEmpty()) alerts.add("هناك ${scheduledSoon.size} استشارات مجدولة خلال 48 ساعة القادمة")

        return DashboardDTO(
            visitsToday = visitsToday,
            waitingToday = waiting,
            completedToday = completed,
            canceledToday = canceled,
            newPatientsToday = newPatients,
            revenueToday = round(revenueToday * 100) / 100.0,
            nextAppointments = nextAppointments,
            visitsPerDoctor = visitsPerDoctor,
            weeklyRevenue = weeklyRevenue,
            visitTypeBreakdown = typeBreakdown,
            avgWaitingMinutes = round(avgWaiting * 100) / 100.0,
            topPatients = topPatients,
            topMedicines = topMedicines,
            alerts = alerts
        )
    }

    // heuristic parser for medicine names from prescription.content
    private fun extractMedicineNames(p: Prescription): List<String> {
        val content = p.content ?: return emptyList()
        // try to parse simple comma / newline separated names or keys like "Medicine: name"
        val tokens = content.split("\n", ",", ";").map { it.trim() }.filter { it.isNotEmpty() }
        val meds = mutableListOf<String>()
        for (t in tokens) {
            // remove dosage numbers, keep words (simple heuristic)
            val cleaned = t.replace(Regex("\\d+mg|\\d+ mg|\\d+ ml|\\(.*?\\)|\\d+\\/\\d+"), "").trim()
            // take first 4 words max
            val words = cleaned.split(Regex("\\s+")).take(4).joinToString(" ")
            if (words.isNotBlank()) meds.add(words)
        }
        return meds
    }
}



