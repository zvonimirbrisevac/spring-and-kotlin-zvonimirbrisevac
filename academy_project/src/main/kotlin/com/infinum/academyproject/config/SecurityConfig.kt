package com.infinum.academyproject.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http {
            cors { }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeRequests {
                authorize(HttpMethod.POST, "/cars", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.GET, "/cars", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "/cars/{id}", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.GET, "/cars/{id}/checkups", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.DELETE, "/cars/{id}", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.POST, "/car-checkups", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "/car-checkups", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "/car-checkups/upcoming", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.DELETE, "/car-checkups", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "/models", permitAll)
            }
            oauth2ResourceServer {
                jwt {}
            }
        }
        return http.build()
    }
}