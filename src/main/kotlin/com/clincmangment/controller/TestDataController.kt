package com.clincmangment.controller

import com.clincmangment.repository.ClincRepository
import com.clincmangment.repository.model.Clinic
import com.clincmangment.repository.model.User
import com.clincmangment.service.UserServiceImpl
import com.clincmangment.utils.Role
import com.clincmangment.utils.SubscriptionType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class TestDataController(
    private val userService: UserServiceImpl,
    private val clinics: ClincRepository
) {
    // إنشاء عيادة جديدة
    @GetMapping("/create-clinic")
    fun createClinic(): String {
        val clinic = Clinic(
            name = "عيادة المستقبل",
            address = "شارع الجامعة، القاهرة",
            subscriptionEndDate = LocalDate.now(),
            subscriptionType = SubscriptionType.ADVANCED,
            checkUpPrice = 400.0,
            followUpPrice = 100.0,


        )
        // تخزينها مؤقتًا في القائمة
        clinics.save(clinic)
        return "Clinic created successfully with ID: ${clinic.id ?: "not persisted yet"}"
    }

    // إنشاء دكتور مرتبط بالعيادة الأخيرة
    @GetMapping("/create-doctor")
    fun createDoctor(): String {
        if (clinics.findAll().isEmpty()) return "No clinic available. Create a clinic first."

        val clinic = clinics.findAll().last()
        val doctor = userService.createUser(
            username = "dr1",
            rawPassword = "1234",
            role = Role.DOCTOR,
            fullName = "Dr. Mohamed",
            phone = "01000000003",
            email = "dr.ahmed@example.com",
            clinic = clinic
        )
        return "Doctor ${doctor.fullName} created successfully for clinic ${clinic.name}"
    }

    @PostMapping("/create-doctor")
    fun createDoctor0(@RequestBody doctor: User): String {
        if (clinics.findAll().isEmpty()) return "No clinic available. Create a clinic first."
        val clinic = clinics.findAll().last()
        val doctor = userService.createUser(
            username = doctor.fullName,
            rawPassword = doctor.password,
            role = Role.DOCTOR,
            fullName = doctor.fullName,
            phone = doctor.phone,
            email = doctor.email,
            clinic = clinic
        )
        return "Doctor ${doctor.fullName} created successfully for clinic ${clinic.name}"
    }

    @PostMapping("/create-clinic")
    fun createClinic(@RequestBody clinic: Clinic): String {
        return try {
            clinics.save(clinic).toString()
        } catch (e: Exception) {
            "${e.message}"
        }
    }
}

