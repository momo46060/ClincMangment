package com.clincmangment.service

import com.clincmangment.repository.UserRepository
import com.clincmangment.repository.model.Clinic
import com.clincmangment.repository.model.User
import com.clincmangment.utils.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) {

    // تسجيل الدخول
    fun login(phone: String, password: String): User? {
        val user = userRepository.findByPhone(phone).orElse(null)
        return if (user != null && user.password == password) user else null
    }

    // إنشاء مستخدم جديد وربطه بالعيادة
    fun createUser(username: String, rawPassword: String, role: Role,
                   fullName: String, clinic: Clinic, phone: String? = null, email: String? = null): User {

        if (userRepository.existsByPhone(username)) {
            throw IllegalArgumentException("Username already exists")
        }

        val user = User(
            password = rawPassword,
            role = role,
            fullName = fullName,
            phone = phone,
            email = email,
            clinic = clinic // ربط المستخدم بالعيادة
        )

        return userRepository.save(user)
    }

    fun findByPhone(phone: String): Optional<User> = userRepository.findByPhone(phone)

    fun findByFullName(fullName: String): Optional<User> = userRepository.findByFullName(fullName)

    // جلب المستخدمين بحسب الدور والعيادة
    fun getUsersByRoleAndClinic(role: Role, clinic: Clinic, pageable: Pageable): Page<User> =
        userRepository.findAllByRoleAndClinic(role, clinic, pageable)
}
