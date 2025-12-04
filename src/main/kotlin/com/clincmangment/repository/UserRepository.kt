package com.clincmangment.repository

import com.clincmangment.model.Clinic
import com.clincmangment.model.User
import com.clincmangment.utils.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.clinic WHERE u.id = :id")
    fun findByIdWithClinic(@Param("id") id: Long): User
    fun findByPhone(phone: String): User?
    fun findByFullName(fullName: String): User
    fun existsByPhone(phone: String): Boolean
    fun findAllByRole(role: Role, pageable: Pageable): Page<User>
    fun findAllByRoleAndClinic(role: Role, clinic: Clinic): List<User>
    fun findByEmail(email: String): User?
    fun findAllByClinicIdAndIdNot(clinicId: Long, userId: Long): List<User>




}