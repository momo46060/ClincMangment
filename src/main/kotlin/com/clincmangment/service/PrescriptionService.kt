package com.clincmangment.service

import com.clincmangment.repository.PrescriptionRepository
import com.clincmangment.repository.PrescriptionTemplateRepository
import com.clincmangment.repository.VisitRepository
import com.clincmangment.repository.model.Prescription
import com.clincmangment.repository.model.PrescriptionTemplate
import com.clincmangment.repository.model.User
import com.clincmangment.repository.model.Visit
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PrescriptionService(
    private val prescriptionRepository: PrescriptionRepository,
    private val visitRepository: VisitRepository,
    private val templateRepository: PrescriptionTemplateRepository,
    private val prescriptionTemplateRepository: PrescriptionTemplateRepository,
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
//    fun getPrescriptionsByVisit(visitId: Long): List<Prescription> {
//        return prescriptionRepository.findAllByVisitId(visitId)
//    }

    @Transactional
    fun savePrescription(visitId: Long, content: String): Prescription {
        val prescription = Prescription(visit = Visit(id = visitId), content = content)
        return prescriptionRepository.save(prescription)
    }

    fun getTemplatesByDoctor(doctor: User): List<PrescriptionTemplate> {
        return templateRepository.findAllByDoctor(doctor)
    }

    @Transactional
    fun saveTemplate(name: String, content: String, doctor: User): PrescriptionTemplate {
        val template = PrescriptionTemplate(name = name, content = content, doctor = doctor)
        return templateRepository.save(template)
    }


    fun getTemplateById(id: Long, doctor: User): PrescriptionTemplate {
        return prescriptionTemplateRepository.findByIdAndDoctor( id,doctor)
    }

//    fun getTemplatesByDoctor(doctor: User): List<PrescriptionTemplate> {
//        return prescriptionTemplateRepository.findByDoctorOrderByNameAsc(doctor)
//    }


}