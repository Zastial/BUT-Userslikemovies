package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.domain.Movie
import com.userslikemovies.springapi.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaRepositoryMovie : JpaRepository<Movie, String>
interface JpaRepositoryUser : JpaRepository<User, String>