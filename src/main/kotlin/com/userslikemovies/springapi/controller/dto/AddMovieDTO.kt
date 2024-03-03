package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.Movie
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class AddMovieDTO(
        @Schema(description = "The id of the movie", example = "1")
        @field:Size(min = 0) val movieId : Int
)