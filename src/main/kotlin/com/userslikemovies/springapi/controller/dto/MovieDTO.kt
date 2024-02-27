package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.Movie

data class MovieDTO(val id : Int, val name : String, val releaseDate : String) {
    companion object {
        fun fromMovie(movie: Movie) : MovieDTO {
            return MovieDTO(movie.id, movie.name, movie.releaseDate)
        }
    }
}