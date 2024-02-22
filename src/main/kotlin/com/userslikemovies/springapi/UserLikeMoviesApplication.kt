package com.userslikemovies.springapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserLikeMoviesApplication

fun main(args: Array<String>) {
	runApplication<UserLikeMoviesApplication>(*args)
}
