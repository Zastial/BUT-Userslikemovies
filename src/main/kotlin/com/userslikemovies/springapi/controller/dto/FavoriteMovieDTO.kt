package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.Movie
import io.swagger.v3.oas.annotations.media.Schema

data class FavoriteMovieDTO(
    @Schema(description = "The id of the movie", example = "1")
    val id : Int,
    @Schema(description = "The name of the movie", example = "Titanic")
    val name : String,
    @Schema(description = "The release date of the movie", example = "1993-05")
    val releaseDate : String,
    @Schema(description = "The number of users who liked the movie", example = "5404")
    val favoriteCount : Int
) {
    companion object {
        fun fromMovie(movie: Movie): MovieDTO {
            return MovieDTO(movie.id, movie.name, movie.releaseDate)
        }
    }

    fun toMovie(): Movie {
        return Movie(this.id, this.name, this.releaseDate)
    }
}