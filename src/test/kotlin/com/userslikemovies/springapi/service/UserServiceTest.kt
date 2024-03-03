package com.userslikemovies.springapi.service

import com.userslikemovies.springapi.Exceptions.MovieNotFoundException
import com.userslikemovies.springapi.controller.dto.FavoriteMovieDTO
import com.userslikemovies.springapi.domain.User
import com.userslikemovies.springapi.repository.IUserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: IUserRepository
    @Test
    fun getUsers() {
        val listUser = listOf(User("john@example.com", "John", "Doe", 25))
        `when`(userRepository.getUsers()).thenReturn(listUser)

        val result = userService.getUsers(null)

        assertTrue(result.isSuccess)
        assertEquals(listUser, result.getOrNull())
    }

    @Test
    fun getUserByEmail() {
        val user = (User("john@example.com", "John", "Doe", 25))
        `when`(userRepository.getUserByEmail(user.email)).thenReturn(Pair(user, null))

        val result = userService.getUserByEmail(user.email)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun createUser() {
        val user = (User("john@example.com", "John", "Doe", 25))
        `when`(userRepository.createUser(user)).thenReturn(Pair(user, null))

        val result = userService.createUser(user)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun updateUser() {
        val user = (User("john@example.com", "John", "Doe", 25))
        val updatedUser = (User("john@example.com", "Alexandre", "Doe", 64))

        `when`(userRepository.updateUser(user.email, updatedUser)).thenReturn(Pair(user, null))

        val result = userService.updateUser(user.email, updatedUser)

        assertTrue(result.isSuccess)
        assertEquals(updatedUser, result.getOrNull())
    }

    @Test
    fun deleteUser() {
        val user = (User("john@example.com", "John", "Doe", 25))
        `when`(userRepository.deleteUser(user.email)).thenReturn(Pair(user, null))

        val result = userService.deleteUser(user.email)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun addUserFavoriteMovie() {
        val user = (User("john@example.com", "John", "Doe", 25))
        `when`(userRepository.addUserFavoriteMovie(user.email, 1)).thenReturn(Pair(user, null))

        val result = userService.addUserFavoriteMovie(user.email, 1)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun removeUserFavoriteMovie() {
        val user = (User("john@example.com", "John", "Doe", 25))
        `when`(userRepository.removeUserFavoriteMovie(user.email, 1)).thenReturn(Pair(user, null))

        val result = userService.removeUserFavoriteMovie(user.email, 1)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun movieDeleted() {
        `when`(userRepository.movieDeleted(1)).thenReturn(null)

        val result = userService.movieDeleted(1)

        assertNull(result)
    }

    @Test
    fun movieDeletedException() {
        val expectedError = MovieNotFoundException()
        `when`(userRepository.movieDeleted(15)).thenReturn(expectedError)

        val result = userService.movieDeleted(15)

        assertEquals(expectedError, result)
    }

    @Test
    fun getMoviePreferenceNumber() {
        val expectedMoviePreference = FavoriteMovieDTO(1, "Titanic", "2-1950", 2)
        `when`(userRepository.getMoviePreferenceNumber(1)).thenReturn(Pair(expectedMoviePreference, null))

        val result = userService.getMoviePreferenceNumber(1)

        assertTrue(result.isSuccess)
        assertEquals(expectedMoviePreference, result.getOrNull())
    }
}