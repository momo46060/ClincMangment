package com.clincmangment.model

import com.clincmangment.utils.VisitType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "visits", indexes = [
        Index(name = "idx_visits_patient", columnList = "patient_id"),
        Index(name = "idx_visits_doctor", columnList = "doctor_id"),
        Index(name = "idx_visits_visit_date", columnList = "visitDate")
    ]
)
data class Visit(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "nurse_name")
    var nurseName: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    val patient: Patient? = null,

    // الممرضة التي سجلت الزيارة (يمكن أن تكون null لو النظام سجلها بآلية أخرى)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id")
    val nurse: User? = null,

    // الدكتور الذي عُيِّن للزيارة (يمكن أن يظل null حتى يعين)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    var doctor: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val visitType: VisitType? = null,

    @Column(nullable = false)
    val visitDate: LocalDateTime = LocalDateTime.now(), // تاريخ الكشف الفعلي

    @Column(nullable = true, length = 100000)
    var complaint: String? = null,       // سبب المراجع

    @Column(nullable = true, length = 100000)
    var diagnosis: String? = null,       // يكتبه الدكتور بعد الكشف

    @Column(nullable = true, length = 100000)
    var prescription: String? = null,    // وصفة / أدوية (ببساطة نص أو JSON لاحقًا)

    // إذا الدكتور حدد موعد استشارة مستقبلية
    var scheduledConsultation: LocalDateTime? = null,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @Column(name = "status")
    var status: String = "في الانتظار",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    var clinic: Clinic? = null,
    @Column(nullable = false)
    var basicPrice: Double = 0.0,
    @Column(nullable = false)
    var addOnesPrice: Double = 0.0,
    @Column(nullable = false)
    var visitPrice: Double = basicPrice+addOnesPrice,

// الحالة الافتراضية
)
