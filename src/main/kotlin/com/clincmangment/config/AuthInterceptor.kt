package com.clincmangment.config

import com.clincmangment.repository.model.User
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor : HandlerInterceptor  {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val uri = request.requestURI

        // السماح بصفحات معينة بدون تسجيل دخول
        if (uri.startsWith("/login") ||
            uri.startsWith("/css") ||
            uri.startsWith("/js") ||
            uri.startsWith("/images")) {
            return true
        }

        val user = request.session.getAttribute("loggedUser") as? User

        if (user == null) {
            response.sendRedirect("/login")
            return false
        }

        return true
    }
}