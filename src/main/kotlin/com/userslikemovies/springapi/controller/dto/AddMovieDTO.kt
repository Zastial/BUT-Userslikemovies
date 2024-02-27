package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.Movie
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class AddMovieDTO(
        @field:Size(min = 0) val movieId : Int
)