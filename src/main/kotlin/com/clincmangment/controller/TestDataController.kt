package com.clincmangment.controller

import com.clincmangment.repository.ClincRepository
import com.clincmangment.model.Clinic
import com.clincmangment.model.User
import com.clincmangment.service.ChatMessageService
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
    private val clinics: ClincRepository,
    private val chatMessageService: ChatMessageService
) {
    @GetMapping("/chat/unread-counts")
    fun getUnreadCounts(): MutableMap<Long?, Int?> {
        return chatMessageService.getAllUnreadCounts()
    }

    // إنشاء عيادة جديدة
    @GetMapping("/create-clinic")
    fun createClinic(): String {
        val clinic = Clinic(
            name = "test",
            address = "test",
            subscriptionEndDate = LocalDate.now(),
            subscriptionType = SubscriptionType.ADVANCED,
            checkUpPrice = 0.0,
            followUpPrice = 0.0,
            phone = "01000000000"
  )
        // تخزينها مؤقتًا في القائمة
        clinics.save(clinic)
        return "Clinic created successfully with ID: ${clinic.id ?: "not persisted yet"}"
    }

    // إنشاء دكتور مرتبط بالعيادة الأخيرة
    @GetMapping("/create-admin")
    fun createDoctor(): String {
        val clinic = Clinic(
            name = "test",
            address = "test",
            subscriptionEndDate = LocalDate.now(),
            subscriptionType = SubscriptionType.ADVANCED,
            checkUpPrice = 0.0,
            followUpPrice = 0.0,
            phone = "01000000000"
        )
        // تخزينها مؤقتًا في القائمة
        clinics.save(clinic)
        val doctor = userService.createUser(
            username = "ADMIN",
            rawPassword = "1234",
            role = Role.ADMIN,
            fullName = "ADMIN",
            phone = "01000000001",
            email = "ADMIN",
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




