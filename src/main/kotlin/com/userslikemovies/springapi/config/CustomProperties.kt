package com.userslikemovies.springapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties("movies.api.baseurl")
data class CustomProperties(val url : String = "") {
}