package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.Movie

data class FavoriteMovieDTO(val id : Int, val name : String, val releaseDate : String, val favoriteCount : Int) {
    companion object {
        fun fromMovie(movie: Movie): MovieDTO {
            return MovieDTO(movie.id, movie.name, movie.releaseDate)
        }
    }

    fun toMovie(): Movie {
        return Movie(this.id, this.name, this.releaseDate)
    }
}