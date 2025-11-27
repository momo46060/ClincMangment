package com.clincmangment.service

import com.clincmangment.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

/**
 * إعادة ضبط كلمات المرور لكلمة مرور واحدة لكل المستخدمين
 * استخدمه للاختبار السريع
 */
//@Component
class SimplePasswordReset(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        println("🔄 بدء إعادة ضبط كلمات المرور...")

        // كلمة المرور الافتراضية لكل المستخدمين
        val defaultPassword = "123456"

        val users = userRepository.findAll()
        var count = 0

        users.forEach { user ->
            user.password = passwordEncoder.encode(defaultPassword)
            userRepository.save(user)
            println("✅ ${user.phone} → كلمة المرور: $defaultPassword")
            count++
        }

        println("=" .repeat(50))
        println("✅ تم إعادة ضبط كلمات المرور لـ $count مستخدم")
        println("📱 كلمة المرور لجميع المستخدمين: $defaultPassword")
        println("⚠️  الآن احذف أو عطّل هذا الملف!")
        println("=" .repeat(50))
    }
}