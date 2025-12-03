package com.clincmangment.repository.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "prescription_templates")
data class PrescriptionTemplate(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,  // اسم القالب

    @Column(nullable = false, length = 5000)
    val content: String, // JSON يحتوي على الأدوية والجرعات

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    val doctor: User,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null
)
