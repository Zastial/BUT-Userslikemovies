package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Objet représentant un utilisateur")
data class UserDTO(
        @field:Email
        @Schema(description = "Email de l'utilisateur", example = "john@example.com")
        val email: String,

        @field:Size(min = 2, max = 25)
        @Schema(description = "Prénom de l'utilisateur", example = "John")
        val firstName: String,

        @field:Size(min = 2, max = 25)
        @Schema(description = "Nom de famille de l'utilisateur", example = "Doe")
        val lastName: String,

        @field:Size(min = 15, max = 99)
        @Schema(description = "Âge de l'utilisateur", example = "25")
        val age: Int,

        @field:Size(min = 1, max = 30)
        @Schema(description = "Liste des films préférés de l'utilisateur")
        val favoriteMovies: MutableList<MovieDTO?>
) {
        fun asUser() = User(email, firstName, lastName, age, favoriteMovies.map { it?.toMovie() }.toMutableList())
}