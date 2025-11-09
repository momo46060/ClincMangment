package com.clincmangment.repository.model

import com.clincmangment.utils.Role
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "users", indexes = [
        Index(name = "idx_users_phone", columnList = "phone")
    ]
)
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var password: String = "",

    @Column(nullable = false)
    var role: Role = Role.PATIENT,

    @Column(nullable = false)
    var fullName: String = "",

    @Column(nullable = true, unique = true)
    var phone: String? = null,

    @Column(nullable = true)
    var email: String? = null,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    var clinic: Clinic

)