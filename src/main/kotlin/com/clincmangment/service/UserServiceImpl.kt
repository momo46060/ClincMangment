package com.clincmangment.service

import com.clincmangment.repository.UserRepository
import com.clincmangment.model.Clinic
import com.clincmangment.model.User
import com.clincmangment.utils.Role
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder

) {
    fun findById(id: Long): Optional<User> = userRepository.findById(id)

    fun findByIdWithClinic(id: Long): Optional<User> = userRepository.findById(id)

    fun login(phone: String, password: String): User? {
        val user = userRepository.findByPhone(phone)
        return if (user != null && user.password == password) user else null
    }

    fun createUser(username: String, rawPassword: String, role: Role,
                   fullName: String, clinic: Clinic,
                   phone: String? = null, email: String? = null): User {
        if (userRepository.existsByPhone(username)) {
            throw IllegalArgumentException("Username already exists")
        }
        val encryptedPassword = passwordEncoder.encode(rawPassword)

        val user = User(
            password = encryptedPassword,
            role = role,
            fullName = fullName,
            phone = phone,
            email = email,
            clinic = clinic
        )

        return userRepository.save(user)
    }

    fun findByPhone(phone: String): User? = userRepository.findByPhone(phone)

    fun findAllByDoctors(role: Role): List<User>? = userRepository.findAllByRole(role)

    fun findByFullName(fullName: String): User? = userRepository.findByFullName(fullName)

    // جلب المستخدمين بحسب الدور والعيادة
    fun getUsersByRoleAndClinic(role: Role, clinic: Clinic): List<User> =
        userRepository.findAllByRoleAndClinic(role, clinic)
}
