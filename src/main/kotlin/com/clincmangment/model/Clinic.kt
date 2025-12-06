package com.clincmangment.model

import com.clincmangment.utils.SubscriptionType
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "clinics")
data class Clinic(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = true)
    var address: String? = null,

    var checkUpPrice: Double = 0.0,

    var followUpPrice: Double = 0.0,

    var subscriptionEndDate: LocalDate ,

    var phone:String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var subscriptionType: SubscriptionType = SubscriptionType.ADVANCED

)
