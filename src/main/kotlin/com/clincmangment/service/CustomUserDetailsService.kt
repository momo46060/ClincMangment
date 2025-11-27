package com.clincmangment.service

import com.clincmangment.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(phone: String): UserDetails {
        // جلب المستخدم من قاعدة البيانات بناءً على رقم الهاتف
        val user = userRepository.findByPhone(phone)
            ?: throw UsernameNotFoundException("المستخدم غير موجود برقم الهاتف: $phone")

        // ✅ طباعة الـ role للتأكد
        println("🔍 تحميل المستخدم: ${user.fullName} | Role من DB: ${user.role}")

        val authority = SimpleGrantedAuthority("ROLE_${user.role.name}")
        println("🔑 Authority المُنشأ: $authority")

        // إنشاء UserDetails مع الـ role من قاعدة البيانات
        return org.springframework.security.core.userdetails.User(
            user.phone,  // username = phone
            user.password,  // كلمة المرور المشفرة
            listOf(authority)  // ✅ الـ role من الجدول
        )
    }
}