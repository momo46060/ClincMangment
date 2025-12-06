package com.clincmangment.config

import com.clincmangment.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val customAuthenticationSuccessHandler: CustomAuthenticationSuccessHandler
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(customUserDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/login", "/h2-console/**", "/css/**", "/js/**",
                        "/images/**", ).permitAll()
                    .requestMatchers("/doctor/**").hasRole("DOCTOR")
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/nurse/**").hasRole("NURSE")
                    .requestMatchers("/patients/**").hasAnyRole("DOCTOR", "NURSE", "PATIENT")
                    .requestMatchers("/visits/**").hasAnyRole("DOCTOR", "NURSE", "PATIENT")
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("phone")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler)  // ✅ استخدام الـ handler المخصص
                    .failureUrl("/login?error=true")
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            }
            .csrf { it.disable() }
            .headers { it.frameOptions { frame -> frame.disable() } }

        http.authenticationProvider(authenticationProvider())

        return http.build()
    }
}