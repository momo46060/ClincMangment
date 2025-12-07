package com.clincmangment.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "visit_services")
data class VisitService(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "visit_id")
    @JsonIgnore // ⭐ تجاهل تسلسل كيان Visit بأكمله
    val visit: Visit,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    val service: ClinicService,

    @Column(nullable = false)
    var price: Double
)
