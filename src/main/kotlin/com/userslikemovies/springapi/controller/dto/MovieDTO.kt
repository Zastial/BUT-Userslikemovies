package com.userslikemovies.springapi.controller.dto

import com.userslikemovies.springapi.domain.Movie
import io.swagger.v3.oas.annotations.media.Schema

data class MovieDTO(
    @Schema(description = "Identifiant du film", example = "1")
    val id : Int,
    @Schema(description = "Nom du film", example = "Titanic")
    val name : String,
    @Schema(description = "Date de sortie du film", example = "1997-12-19")
    val releaseDate : String
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