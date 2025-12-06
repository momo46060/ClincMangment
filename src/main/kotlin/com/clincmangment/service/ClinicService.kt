package com.clincmangment.service

import com.clincmangment.repository.UserRepository
import com.clincmangment.repository.ClincRepository
import com.clincmangment.model.Clinic
import com.clincmangment.utils.SubscriptionType
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ClinicService(
    private val clinicRepository: ClincRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun updateClinicPrices(userId: Long?, checkUpPrice: Double, followUpPrice: Double) {
        val user = userRepository.findById(userId!!).orElseThrow()
        val clinic = user.clinic
        clinic.checkUpPrice = checkUpPrice
        clinic.followUpPrice = followUpPrice
        clinicRepository.save(clinic)
    }

    fun getAllClinics(): List<Clinic> = clinicRepository.findAll()
    fun findClinicById(id: Long): Clinic = clinicRepository.findById(id).orElseThrow()

    fun isSubscriptionActive(clinic: Clinic): Boolean {
        val today = LocalDate.now()
        val endDate = clinic.subscriptionEndDate
        return endDate == null || !today.isAfter(endDate) // true لو الاشتراك شغال
    }

    fun createClinic(clinic: Clinic): String {

        // تخزينها مؤقتًا في القائمة
        clinicRepository.save<Clinic>(clinic)
        return "Clinic created successfully with ID: ${clinic.id ?: "not persisted yet"}"
    }

    fun checkSubscriptionAndThrow(clinic: Clinic): String {
        if (!isSubscriptionActive(clinic)) {
            return "اشتراك العيادة انتهى. يرجى تجديد الاشتراك للوصول للميزات المدفوعة."
        } else {
            return ""
        }
    }
}
