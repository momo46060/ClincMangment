package com.clincmangment.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    // 1. Allow all requests to the H2 console path
                    .requestMatchers("/h2-console/**").permitAll()
                    // Allow all other requests (as per your original code)
                    .anyRequest().permitAll()
            }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

            // 2. Disable CSRF globally (as per your original code)
            //    If you prefer to enable CSRF for other endpoints,
            //    you should change 'it.disable()' to 'csrf -> csrf.ignoringRequestMatchers("/h2-console/**")'
            .csrf { it.disable() }

            // 3. Important: Disable X-Frame-Options for the H2 console to allow it to render in an iframe
            .headers { headers ->
                headers.frameOptions { frameOptions ->
                    frameOptions.disable()
                }
            }

        return http.build()
    }
}