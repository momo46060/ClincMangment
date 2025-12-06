package com.clincmangment.config

import com.clincmangment.repository.UserRepository
import com.clincmangment.utils.Role
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationSuccessHandler(
    private val userRepository: UserRepository
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        // جلب رقم الهاتف من authentication
        val phone = authentication.name

        // جلب بيانات المستخدم من قاعدة البيانات (مع الـ role)
        val user = userRepository.findByPhone(phone)

        if (user != null) {
            // حفظ المستخدم الكامل في الـ session (فيه الـ role)
            request.session.setAttribute("loggedUser", user)

            // ✅ تحديد الصفحة المناسبة حسب الـ role من قاعدة البيانات
            val targetUrl = when (user.role) {
                Role.DOCTOR -> "/doctor/dashboard"
                Role.NURSE -> "/nurse/dashboard"
                Role.ADMIN -> "/admin/manage"
                Role.PATIENT -> "/patients/view/${user.phone}"
            }

            println("✅ تسجيل دخول: ${user.fullName} | Role: ${user.role} | Redirect: $targetUrl")

            // إعادة التوجيه للصفحة المناسبة
            response.sendRedirect(targetUrl)
        } else {
            // في حالة حدوث خطأ، إعادة التوجيه للصفحة الرئيسية
            println("❌ خطأ: المستخدم غير موجود")
            response.sendRedirect("/")
        }
    }
}