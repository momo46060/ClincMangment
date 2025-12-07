package com.clincmangment.repository

import com.clincmangment.model.VisitService
import org.springframework.data.jpa.repository.JpaRepository

interface VisitServiceRepository : JpaRepository<VisitService, Long> {
    fun findByVisitId(visitId: Long): List<VisitService>
}
