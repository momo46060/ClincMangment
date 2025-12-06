package com.clincmangment.utils

import com.clincmangment.model.User
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpSession

enum class Role { DOCTOR, NURSE, PATIENT, ADMIN }
enum class VisitType(val label: String) {
    CHECKUP("كشف"),
    CONSULTATION("استشارة");
}

enum class VisitStatus(val label: String) {
    WAITING("في الانتظار"),
    IN_PROGRESS("جاري الكشف"),
    COMPLETED("تم الكشف"),
    CANCELED("تم الإلغاء")
}
enum class SubscriptionType {
    BASIC,      // الباقة الأساسية
    ADVANCED    // الباقة المتقدمة
}

fun getCurrentUser(httpSession: HttpSession): Any {
    val currentUser = httpSession.getAttribute("loggedUser") as? User
        ?: return "redirect:/login"
    return currentUser
}

data class EditVisitForm(
    val visitId: Long = 0,
    val patientCode: String = "",
    val diagnosis: String = "",
    val prescription: String = "",
    val scheduledConsultation: String = ""
)