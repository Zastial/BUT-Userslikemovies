package com.userslikemovies.springapi.domain

import com.userslikemovies.springapi.controller.dto.MovieDTO
import jakarta.persistence.*

@Entity
@Table(name = "utilisateur")
class User(
        @Id
        var email: String,
        var firstName: String,
        var lastName: String,
        var age: Int,
        @ManyToMany(cascade = [CascadeType.ALL])
        var movies: List<Movie?> = listOf()
)