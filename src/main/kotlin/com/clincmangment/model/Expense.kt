package com.clincmangment.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "expenses")
data class Expense(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,               // اسم المصروف

    @Column(nullable = false)
    var amount: Double,              // المبلغ

    @Column(nullable = true, length = 2000)
    var notes: String? = null,       // ملاحظات

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    var clinic: Clinic,              // العيادة التابعة

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var createdBy: User,             // من أدخل المصروف

    @CreationTimestamp
    val createdAt: LocalDateTime? = null
)
