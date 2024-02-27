package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserCreationDTO(
        @field:Email val email: String,
        @field:Size(min = 2, max = 25) val firstName: String,
        @field:Size(min = 2, max = 25) val lastName: String,
        @field:Size(min = 15, max = 99) val age: Int,
) {
    fun asUser() = User(email, firstName, lastName, age, mutableListOf())
}
