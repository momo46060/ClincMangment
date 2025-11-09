package com.clincmangment.repository.dto

import java.time.LocalDate

// DTO للفورم
data class PatientForm(
    var username: String = "",
    var password: String = "",
    var fullName: String = "",
    var phone: String? = null,
    var birthDate: LocalDate? = null,
    var notes: String? = null
)