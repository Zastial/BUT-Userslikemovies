package com.userslikemovies.springapi.domain

import jakarta.persistence.*

@Entity
@Table(name = "utilisateur")
class User(
        @Id
        @GeneratedValue
        val id : String,
        val email: String,
        val firstName: String,
        val lastName: String,
        val age: Int,
        @ElementCollection
        val moviesId: List<Long> = listOf()
)