package com.clincmangment.model

import jakarta.persistence.*

@Entity
@Table(name = "clinic_services")
data class ClinicService(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var price: Double,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    val doctor: User,
)
