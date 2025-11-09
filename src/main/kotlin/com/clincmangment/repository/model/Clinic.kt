package com.clincmangment.repository.model

import jakarta.persistence.*

@Entity
@Table(name = "clinics")
data class Clinic(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var address: String? = null
)
