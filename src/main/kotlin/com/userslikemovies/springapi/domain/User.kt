package com.userslikemovies.springapi.domain

import jakarta.persistence.*

@Entity
@Table(name = "utilisateur")
class User(
        @Id
        val email: String,
        val firstName: String,
        val lastName: String,
        val age: Int,
        @ElementCollection
        val moviesId: List<Int> = listOf()
)