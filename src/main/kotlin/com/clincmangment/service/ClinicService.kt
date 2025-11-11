package com.clincmangment.service

import com.clincmangment.repository.UserRepository
import com.clincmangment.repository.model.ClincRepository
import com.clincmangment.repository.model.Clinic
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ClinicService(
    private val clinicRepository: ClincRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun updateClinicPrices(userId: Long?, consultationPrice: Double, followUpPrice: Double) {
        val user = userRepository.findById(userId!!).orElseThrow()
        val clinic = user.clinic
        clinic.consultationPrice = consultationPrice
        clinic.followUpPrice = followUpPrice
        clinicRepository.save(clinic)
    }

    fun findClinicById(id: Long): Clinic = clinicRepository.findById(id).orElseThrow()
}
