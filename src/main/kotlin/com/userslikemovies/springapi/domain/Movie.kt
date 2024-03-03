package com.userslikemovies.springapi.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
@Entity
class Movie(
        @Id
        val id : Int,
        val name : String,
        val releaseDate : String
)