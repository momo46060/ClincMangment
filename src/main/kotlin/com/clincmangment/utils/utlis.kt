package com.clincmangment.utils

import com.clincmangment.repository.model.User
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