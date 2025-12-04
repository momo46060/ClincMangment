package com.clincmangment.utils

import com.clincmangment.model.User
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpSession

enum class Role { DOCTOR, NURSE, PATIENT }
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
// في service أو utility class
fun formatPrescriptionForDisplay(prescriptionJson: String): String {
    return try {
        if (prescriptionJson.isBlank() || prescriptionJson == "null") {
            return "لا توجد أدوية"
        }

        val objectMapper = ObjectMapper()
        val items: List<Map<String, String>> = objectMapper.readValue(prescriptionJson,
            object : TypeReference<List<Map<String, String>>>() {})

        if (items.isEmpty()) {
            return "لا توجد أدوية"
        }

        val formatted = StringBuilder()
        items.forEachIndexed { index, item ->
            val name = item["name"] ?: "دواء بدون اسم"
            val dose = item["dose"]
            val times = item["times"]
            val duration = item["duration"]
            val notes = item["notes"]

            formatted.append("${index + 1}. $name\n")
            if (!dose.isNullOrBlank()) {
                formatted.append("   الجرعة: $dose\n")
            }
            if (!times.isNullOrBlank()) {
                formatted.append("   عدد المرات: $times مرات/يوم\n")
            }
            if (!duration.isNullOrBlank()) {
                formatted.append("   المدة: $duration يوم\n")
            }
            if (!notes.isNullOrBlank()) {
                formatted.append("   ملاحظات: $notes\n")
            }
            formatted.append("\n")
        }

        formatted.toString()
    } catch (e: Exception) {
        // إذا لم يكن JSON، اعرض النص كما هو
        prescriptionJson
    }
}