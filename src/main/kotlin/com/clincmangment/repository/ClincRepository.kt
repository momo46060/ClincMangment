package com.clincmangment.repository

import com.clincmangment.repository.model.Clinic
import org.springframework.data.jpa.repository.JpaRepository

interface ClincRepository : JpaRepository<Clinic, Long> {
}