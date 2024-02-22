package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.Movie

data class MovieDTO(val name : String, val releaseDate : String) {
    companion object {
        fun fromMovie(movie: Movie) : MovieDTO {
            return MovieDTO(movie.name, movie.releaseDate)
        }
    }
}