package com.userslikemovies.springapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Configuration

@ConfigurationProperties("movies.api")
data class CustomProperties(val baseurl : String) {
}