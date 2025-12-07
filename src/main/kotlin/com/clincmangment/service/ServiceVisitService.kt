package com.clincmangment.service

import com.clincmangment.model.VisitService
import com.clincmangment.repository.VisitServiceRepository
import com.clincmangment.repository.dto.VisitServiceDto
import org.springframework.stereotype.Service

@Service
class ServiceVisitService(
    private val visitServiceRepository: VisitServiceRepository
) {
    fun getServicesByVisitId(visitId: Long): List<VisitServiceDto> {
        return visitServiceRepository.findByVisitId(visitId).map { visitService ->
            VisitServiceDto(
                id = visitService.id!!,
                serviceName = visitService.service.name, // سيتم تحميل الخدمة هنا (اذا كانت EAGER أو تم الوصول إليها)
                price = visitService.price
            )
        }
    }
    fun deleteById(id: Long?) = visitServiceRepository.deleteById(id!!)
    fun findByVisitId(visitId: Long) : List<VisitService>
    = visitServiceRepository.findByVisitId(visitId)
    fun save(service: VisitService) = visitServiceRepository.save<VisitService>(service)
}