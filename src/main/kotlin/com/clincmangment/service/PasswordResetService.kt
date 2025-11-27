//package com.clincmangment.service
//
//import com.clincmangment.repository.UserRepository
//import org.springframework.boot.CommandLineRunner
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.stereotype.Component
//
///**
// * إعادة ضبط كلمات المرور
// * امسح PasswordMigrationService القديم واستخدم هذا بدلاً منه
// */
////@Component
//class PasswordResetService(
//    private val userRepository: UserRepository,
//    private val passwordEncoder: PasswordEncoder
//) : CommandLineRunner {
//
//    override fun run(vararg args: String?) {
//        println("🔄 بدء إعادة ضبط كلمات المرور...")
//
//        val users = userRepository.findAll()
//
//        users.forEach { user ->
//            // هنا حط كلمة المرور الأصلية (غير المشفرة) لكل مستخدم
//            // مثال:
//            when (user.phone) {
//                "01234567890" -> {
//                    user.password = passwordEncoder.encode("123456")
//                    userRepository.save(user)
//                    println("✅ تم إعادة ضبط كلمة المرور للمستخدم: ${user.phone}")
//                }
//                "01111111111" -> {
//                    user.password = passwordEncoder.encode("password")
//                    userRepository.save(user)
//                    println("✅ تم إعادة ضبط كلمة المرور للمستخدم: ${user.phone}")
//                }
//                // أضف باقي المستخدمين هنا
//                else -> {
//                    // كلمة مرور افتراضية للمستخدمين الباقيين
//                    user.password = passwordEncoder.encode("123456")
//                    userRepository.save(user)
//                    println("✅ تم ضبط كلمة المرور الافتراضية للمستخدم: ${user.phone}")
//                }
//            }
//        }
//
//        println("✅ تم الانتهاء من إعادة ضبط جميع كلمات المرور")
//        println("⚠️ الآن احذف أو عطّل هذا الملف!")
//    }
//}