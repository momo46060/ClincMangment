package com.clincmangment.repository.model

import org.springframework.data.jpa.repository.JpaRepository

interface ClincRepository : JpaRepository< Clinic,Long> {
}