package com.clincmangment.repository

import com.clincmangment.repository.model.Clinic
import com.clincmangment.repository.model.User
import com.clincmangment.utils.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByPhone(phone: String): Optional<User>
    fun findByFullName(fullName: String): Optional<User>
    fun existsByPhone(phone: String): Boolean
    fun findAllByRole(role: Role, pageable: Pageable): Page<User>
    fun findAllByRoleAndClinic(role: Role, clinic: Clinic, pageable: Pageable): Page<User>
}