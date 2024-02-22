package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class UserDTO(
        @field:Size(min = 1, max = 30) val id: Int?,
        @field:Email val email: String,
        @field:Size(min = 1, max = 30) val firstName: String,
        @field:Size(min = 1, max = 30) val lastName: String,
        @field:Size val age: Int,
        @field:Size(min = 1, max = 30) val moviesId: List<Int?>
) {
    fun asUser() = User(1, email, firstName, lastName, 15, emptyList())
}

fun User.asUserDTO() = UserDTO(this.id, this.email, this.firstName, this.lastName, this.age, this.moviesId)
