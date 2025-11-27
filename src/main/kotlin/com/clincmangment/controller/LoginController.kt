package com.clincmangment.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class LoginController {

    /**
     * عرض صفحة تسجيل الدخول
     */
    @GetMapping("/login")
    fun loginPage(
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) logout: String?,
        model: Model
    ): String {
        if (error != null) {
            model.addAttribute("error", "رقم الهاتف أو كلمة المرور غير صحيحة")
        }
        if (logout != null) {
            model.addAttribute("success", "تم تسجيل الخروج بنجاح")
        }

        // ✅ لازم يرجع اسم الملف بدون .html
        return "login"
    }
}