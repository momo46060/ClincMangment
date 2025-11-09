package com.clincmangment.repository.dto

data class VisitForm(
    var patientCode: String = "",
    var visitType: String = "",
    var complaint: String? = null,
    var doctorPhone: String? = null,
    var status: String = "في الانتظار"
)
