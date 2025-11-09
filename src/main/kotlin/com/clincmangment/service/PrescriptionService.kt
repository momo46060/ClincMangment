package com.clincmangment.service

import com.clincmangment.repository.PrescriptionRepository
import com.clincmangment.repository.VisitRepository
import com.clincmangment.repository.model.Prescription
import org.springframework.stereotype.Service

@Service
class PrescriptionService(
    private val prescriptionRepository: PrescriptionRepository,
    private val visitRepository: VisitRepository  // تحتاجه للوصول للزيارة
) {

    fun addPrescription(visitId: Long, content: String): Prescription {
        val visit = visitRepository.findById(visitId)
            .orElseThrow { IllegalArgumentException("Visit not found with id: $visitId") }

        val prescription = Prescription(
            visit = visit,
            content = content
        )
        return prescriptionRepository.save(prescription)
    }

    fun getPrescriptionsByVisit(visitId: Long): List<Prescription> =
        prescriptionRepository.findAllByVisit_Id(visitId)
}
