package com.clincmangment.model
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "patients", indexes = [
    Index(name = "idx_patient_patient_code", columnList = "patientCode"),
    Index(name = "idx_patient_phone", columnList = "phone")
])
data class Patient(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // رقم المريض الذي تريده يُعطى عند التسجيل (يمكن صياغة: CLINIC-YYYY-XXXX أو أي نمط)
    @Column(nullable = false, unique = true)
    val patientCode: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", unique = true)
    val user: User,   // يمثل بيانات الحساب الأساسية (الاسم، رقم الهاتف، الخ.)

    @Column(nullable = true)
    var birthDate: LocalDate? = null,

    @Column(nullable = true, length = 2000)
    var notes: String? = null,     // ملاحظات طبية/اجتماعية عامة

    @Column(nullable = true)
    var phone: String? = null,     // نسخة للبحث السريع (مكرر من user.phone للسهولة)

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    var clinic: Clinic

)
