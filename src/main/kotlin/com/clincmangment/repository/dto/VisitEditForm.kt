package com.clincmangment.repository.dto

import java.time.LocalDateTime

data class VisitEditForm (
    var visitId: Long = 0,
    var patientCode: String = "",      // لإعادة التوجيه بعد الحفظ
    var diagnosis: String? = null,
    var prescription: String? = null,
    var scheduledConsultation: LocalDateTime? = null
)