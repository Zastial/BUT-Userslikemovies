package com.userslikemovies.springapi.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

class Movie(
        @Id
        @GeneratedValue
        val id : Int,
        val name : String,
        val releaseDate : String
)