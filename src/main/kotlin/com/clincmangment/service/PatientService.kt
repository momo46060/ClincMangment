package com.clincmangment.service

import com.clincmangment.repository.PatientRepository
import com.clincmangment.repository.model.Clinic
import com.clincmangment.repository.model.Patient
import com.clincmangment.repository.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PatientService(
    private val patientRepository: PatientRepository
) {

    // البحث بالرمز أو الهاتف بدون اعتبار العيادة
    fun searchByCodeOrPhone(search: String): List<Patient> {
        return patientRepository.findByPatientCodeContainingIgnoreCaseOrPhoneContaining(search, search)
    }

    // البحث بالرمز أو الهاتف بدون اعتبار العيادة (واحد)
    fun findByCodeOrPhone(keyword: String): Patient? {
        val byCode = patientRepository.findByPatientCode(keyword)
        if (byCode.isPresent) return byCode.get()

        val byPhone = patientRepository.findByPhone(keyword)
        if (byPhone.isPresent) return byPhone.get()

        return null
    }

    // إنشاء مريض جديد
    fun createPatient(patientCode: String, user: User, birthDate: java.time.LocalDate? = null, notes: String? = null, phone: String? = null, clinic: Clinic? = null): Patient {
        if (patientRepository.findByPatientCode(patientCode).isPresent) {
            throw IllegalArgumentException("Patient code already exists")
        }

        val patient = Patient(
            patientCode = patientCode,
            user = user,
            birthDate = birthDate,
            notes = notes,
            phone = phone ?: user.phone,
            clinic = clinic ?: user.clinic
        )

        return patientRepository.save(patient)
    }

    fun searchByName(name: String, pageable: Pageable) =
        patientRepository.searchByName("%$name%", pageable)

    fun findByPatientCode(code: String): Optional<Patient> = patientRepository.findByPatientCode(code)

    fun findByPhone(phone: String): Optional<Patient> = patientRepository.findByPhone(phone)

    fun findByUserId(userId: Long): Optional<Patient> = patientRepository.findByUser_Id(userId)

    // ==================== الدوال الجديدة لدعم العيادات ====================

    fun findByPhoneAndClinic(phone: String, clinic: Clinic): Patient? =
        patientRepository.findByPhoneAndClinic(phone, clinic)

    fun findByPatientCodeAndClinic(code: String, clinic: Clinic): Patient? =
        patientRepository.findByPatientCodeAndClinic(code, clinic)

    fun findByCodeOrPhoneAndClinic(keyword: String, clinic: Clinic): Patient? {
        val byCode = patientRepository.findByPatientCodeAndClinic(keyword, clinic)
        if (byCode != null) return byCode

        val byPhone = patientRepository.findByPhoneAndClinic(keyword, clinic)
        return byPhone
    }

    fun findAllByClinic(clinic: Clinic): List<Patient> = patientRepository.findAllByClinic(clinic)
}
