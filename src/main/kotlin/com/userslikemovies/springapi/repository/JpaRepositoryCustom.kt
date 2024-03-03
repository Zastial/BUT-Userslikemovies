package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaRepositoryUser : JpaRepository<User, String>