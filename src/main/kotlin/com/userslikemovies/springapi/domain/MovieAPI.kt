package com.userslikemovies.springapi.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

class MovieAPI(
        @Id
        @GeneratedValue
        val id : Int,
        val name : String,
        val month: Int,
        val year : Int
) {
        fun toMovie() : Movie {
            val releaseDate = "$year-$month"
            return Movie(id, name, releaseDate)
        }
}