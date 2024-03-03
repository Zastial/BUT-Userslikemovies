package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.config.CustomProperties
import com.userslikemovies.springapi.domain.MovieAPI
import com.userslikemovies.springapi.domain.User
import com.userslikemovies.springapi.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.ResponseEntity
import org.springframework.web.client.getForEntity
import java.util.*

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private lateinit var jpaRepository: JpaRepositoryUser

    @MockBean
    private lateinit var customProperties: CustomProperties

    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = UserRepository(jpaRepository, customProperties)
        `when`(customProperties.apiKey).thenReturn("cetteapikeyestvraimentpuissante")
        jpaRepository.deleteAll()
    }

    @Test
    fun getUsers() {
        val user = User("john@example.com", "John", "Doe", 25)
        jpaRepository.save(user)

        val users = userRepository.getUsers()

        assertThat(users).isNotEmpty
    }

    @Test
    fun getUserByEmail() {
        val user = User("john@example.com", "John", "Doe", 25)
        jpaRepository.save(user)

        val result = userRepository.getUserByEmail(user.email)

        assertThat(result!!.email).isEqualTo(user.email)
        assertThat(result.firstName).isEqualTo(user.firstName)
        assertThat(result.lastName).isEqualTo(user.lastName)
        assertThat(result.age).isEqualTo(user.age)
    }

    @Test
    fun createUser() {
        val user = User("john@example.com", "John", "Doe", 25)
        val result = userRepository.createUser(user)

        assertThat(result.first!!.email).isEqualTo(user.email)
        assertThat(result.first!!.firstName).isEqualTo(user.firstName)
        assertThat(result.first!!.lastName).isEqualTo(user.lastName)
        assertThat(result.first!!.age).isEqualTo(user.age)
    }

    @Test
    fun updateUser() {
        val user = User("john@example.com", "John", "Doe", 25)
        jpaRepository.save(user)

        val updatedUser = User("john@example.com", "Alex", "Doe", 25)
        val result = userRepository.updateUser(user.email, updatedUser)

        assertThat(result.first!!.email).isEqualTo(updatedUser.email)
        assertThat(result.first!!.firstName).isEqualTo(updatedUser.firstName)
        assertThat(result.first!!.lastName).isEqualTo(updatedUser.lastName)
        assertThat(result.first!!.age).isEqualTo(updatedUser.age)
    }

    @Test
    fun deleteUser() {
        val user = User("john@example.com", "John", "Doe", 25)
        jpaRepository.save(user)

        val result = userRepository.deleteUser(user.email)

        assertThat(result!!.email).isEqualTo(user.email)
        assertThat(result.firstName).isEqualTo(user.firstName)
        assertThat(result.lastName).isEqualTo(user.lastName)
        assertThat(result.age).isEqualTo(user.age)
    }

    @Test
    fun addUserFavoriteMovie() {

    }

    @Test
    fun removeUserFavoriteMovie() {

    }

    @Test
    fun movieDeleted() {

    }

    @Test
    fun getMoviePreferenceNumber() {

    }
}