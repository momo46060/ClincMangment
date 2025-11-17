package com.clincmangment.repository.dto

data class DashboardDTO(
    val visitsToday: Long,
    val waitingToday: Long,
    val completedToday: Long,
    val canceledToday: Long,
    val newPatientsToday: Long,
    val revenueToday: Double,
    val nextAppointments: List<AppointmentShortDTO>,
    val visitsPerDoctor: List<SeriesItemDTO>,
    val weeklyRevenue: List<Double>, // 7 قيم للأيام
    val visitTypeBreakdown: Map<String, Long>,
    val avgWaitingMinutes: Double,
    val topPatients: List<PatientVisitsDTO>,
    val topMedicines: List<NameCountDTO>,
    val alerts: List<String>
)

data class AppointmentShortDTO(
    val visitId: Long,
    val patientName: String,
    val doctorName: String?,
    val visitType: String,
    val visitTime: String // formatted
)

data class SeriesItemDTO(val name: String, val value: Long)
data class PatientVisitsDTO(val name: String, val visitsCount: Long)
data class NameCountDTO(val name: String, val count: Long)
