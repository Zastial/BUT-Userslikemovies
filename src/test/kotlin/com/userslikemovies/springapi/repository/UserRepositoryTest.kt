package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.domain.MovieAPI
import com.userslikemovies.springapi.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.http.ResponseEntity
import java.util.*

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private lateinit var jpaRepository: JpaRepositoryUser

    @AfterEach
    fun init() {
        jpaRepository.deleteAll()
    }

    @Test
    fun getUsers() {
        val user = User("john@example.com", "John", "Doe", 25)
        jpaRepository.save(user)

        val result = jpaRepository.findAll()

        assertThat(result).hasSize(1)
    }

    @Test
    fun getUserByEmail() {
        val user = User("john@example.com", "John", "Doe", 25)
        jpaRepository.save(user)

        val result = jpaRepository.findById(user.email)

        assertThat(result).isNotEmpty
    }

    @Test
    fun createUser() {
        val user = User("john@example.com", "John", "Doe", 25)
        val result = jpaRepository.save(user)

        assertThat(result.email).isEqualTo(user.email)
        assertThat(result.firstName).isEqualTo(user.firstName)
        assertThat(result.lastName).isEqualTo(user.lastName)
        assertThat(result.age).isEqualTo(user.age)
    }

    @Test
    fun updateUser() {
        val user = User("john@example.com", "John", "Doe", 25)
        jpaRepository.save(user)

        val updatedUser = User("john@example.com", "Alex", "Doe", 25)
        val result = jpaRepository.save(updatedUser)

        assertThat(result.email).isEqualTo(updatedUser.email)
        assertThat(result.firstName).isEqualTo(updatedUser.firstName)
        assertThat(result.lastName).isEqualTo(updatedUser.lastName)
        assertThat(result.age).isEqualTo(updatedUser.age)
    }

    @Test
    fun deleteUser() {
        val user = User("john@example.com", "John", "Doe", 25)
        jpaRepository.save(user)

        jpaRepository.delete(user)

        val result = jpaRepository.findById(user.email)
        assertThat(result).isEmpty
    }

    @Test
    fun addUserFavoriteMovie() {
        val movieAPI = MovieAPI(1, "Titanic", 2, 1950)
        val user = User("john@example.com", "John", "Doe", 25, mutableListOf())

        user.favoriteMovies.add(movieAPI.toMovie())

        val result = jpaRepository.save(user)

        assertThat(result.favoriteMovies).hasSize(1)
    }

    @Test
    fun removeUserFavoriteMovie() {

    }

    @Test
    fun movieDeleted() {
        val movieAPI = MovieAPI(1, "Titanic", 2, 1950)
        val user = User("john@example.com", "John", "Doe", 25, mutableListOf())
        user.favoriteMovies.add(movieAPI.toMovie())
        jpaRepository.save(user)

        val userUpdated = User("john@example.com", "John", "Doe", 25, mutableListOf())
        jpaRepository.save(userUpdated)

        val result = jpaRepository.findById(user.email).get()
        assertThat(result.favoriteMovies).isEmpty()
    }

    @Test
    fun getMoviePreferenceNumber() {

    }
}