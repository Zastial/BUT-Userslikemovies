package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.Movie
import com.userslikemovies.springapi.domain.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserDTO(
        @field:Email val email: String,
        @field:Size(min = 1, max = 30) val firstName: String,
        @field:Size(min = 1, max = 30) val lastName: String,
        @field:Size val age: Int,
        @field:Size(min = 1, max = 30) val movies: List<Movie?>
) {
    fun asUser() = User(email, firstName, lastName, age, movies)
}
