package com.clincmangment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
@EnableMethodSecurity // ✅ مهم لتفعيل @PreAuthorize
class ClincMangmentApplication

fun main(args: Array<String>) {
    runApplication<ClincMangmentApplication>(*args)
}
