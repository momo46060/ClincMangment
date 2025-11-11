package com.clincmangment.repository.model

import jakarta.persistence.*

@Entity
@Table(name = "clinics")
data class Clinic(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @Column(nullable = false)
    var name: String="",

    @Column(nullable = true)
    var address: String? = null,

    var consultationPrice: Double? = 0.0, // سعر الكشف

    var followUpPrice: Double? = 0.0
)
