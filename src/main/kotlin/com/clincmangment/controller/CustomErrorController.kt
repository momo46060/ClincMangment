package com.clincmangment.controller

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.http.HttpStatus

@Controller
class CustomErrorController : ErrorController {

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest, model: Model): String {
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)?.toString()?.toIntOrNull()

        when (status) {
            HttpStatus.NOT_FOUND.value() -> {
                model.addAttribute("errorTitle", "الصفحة غير موجودة")
                model.addAttribute("errorMessage", "يبدو أنك وصلت إلى رابط غير صحيح أو الصفحة تم نقلها.")
                return "error/404"
            }
            HttpStatus.FORBIDDEN.value() -> {
                model.addAttribute("errorTitle", "تم رفض الوصول")
                model.addAttribute("errorMessage", "ليس لديك صلاحية للوصول إلى هذه الصفحة.")
                return "error/403"
            }
            HttpStatus.INTERNAL_SERVER_ERROR.value() -> {
                model.addAttribute("errorTitle", "خطأ في الخادم")
                model.addAttribute("errorMessage", "حدث خطأ غير متوقع. الرجاء المحاولة لاحقًا.")
                return "error/500"
            }
            else -> {
                model.addAttribute("errorTitle", "حدث خطأ")
                model.addAttribute("errorMessage", "حدث خطأ غير معروف. الرجاء المحاولة لاحقًا.")
                return "error/general"
            }
        }
    }
}
