package com.userslikemovies.springapi.logging

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Profile("dev")
class DevLoggingConfiguration : WebMvcConfigurer {

    private val logger = LoggerFactory.getLogger(DevLoggingConfiguration::class.java)

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor())
    }

    fun loggingInterceptor(): HandlerInterceptor {
        return object : HandlerInterceptor {
            override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
                logger.debug("Request received: ${request.method} ${request.requestURI}")
                return true
            }
        }
    }
}