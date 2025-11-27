//package com.clincmangment.service
//
//import com.clincmangment.repository.UserRepository
//import org.springframework.boot.CommandLineRunner
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.stereotype.Component
//
///**
// * سكريبت لتشفير كلمات المرور الموجودة
// * هيشتغل مرة واحدة عند تشغيل التطبيق
// */
////@Component
//class PasswordMigrationService(
//    private val userRepository: UserRepository,
//    private val passwordEncoder: PasswordEncoder
//) : CommandLineRunner {
//
//    override fun run(vararg args: String?) {
//        val users = userRepository.findAll()
//
//        users.forEach { user ->
//            // تحقق إذا كانت كلمة المرور مش مشفرة
//            // كلمات المرور المشفرة بـ BCrypt بتبدأ بـ $2a$ أو $2b$ أو $2y$
//            if (!user.password.startsWith("\$2")) {
//                val plainPassword = user.password
//                user.password = passwordEncoder.encode(plainPassword)
//                userRepository.save(user)
//                println("تم تشفير كلمة المرور للمستخدم: ${user.phone}")
//            }
//        }
//
//        println("✅ تم الانتهاء من تشفير كلمات المرور")
//    }
//}