package com.clincmangment.repository.dto

data class VisitServiceDto(
    val id: Long,
    val serviceName: String, // جلب الاسم مباشرة من الخدمة المرتبطة
    val price: Double
    // لا يتم تضمين حقول Visit أو Patient الكاملة هنا لتجنب التحميل الكسول
)

