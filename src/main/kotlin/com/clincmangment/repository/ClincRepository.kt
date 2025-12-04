package com.clincmangment.repository

import com.clincmangment.model.Clinic
import org.springframework.data.jpa.repository.JpaRepository

interface ClincRepository : JpaRepository<Clinic, Long> {
}