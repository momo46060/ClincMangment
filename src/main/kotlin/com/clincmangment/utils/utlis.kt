package com.clincmangment.utils

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
