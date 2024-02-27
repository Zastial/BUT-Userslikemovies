package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserDTO(
        @field:Email val email: String,
        @field:Size(min = 2, max = 25) val firstName: String,
        @field:Size(min = 2, max = 25) val lastName: String,
        @field:Size(min = 15, max = 99) val age: Int,
        @field:Size(min = 1, max = 30) val favoriteMovies: MutableList<MovieDTO?>
) {
    fun asUser() = User(email, firstName, lastName, age, favoriteMovies.map { it?.toMovie() }.toMutableList())
}
