package com.userslikemovies.springapi.controller

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.userslikemovies.springapi.config.CustomProperties
import com.userslikemovies.springapi.controller.dto.*
import com.userslikemovies.springapi.domain.User
import com.userslikemovies.springapi.service.IUserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(UserController::class)
@ExtendWith(MockitoExtension::class)
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: IUserService
    @MockBean
    private lateinit var customProperties: CustomProperties

    @BeforeEach
    fun setUp() {
        `when`(customProperties.apiKey).thenReturn("cetteapikeyestvraimentpuissante")
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun getUsers_WithAgeParameter_Ok() {
        val listUser = listOf(User("john@example.com", "John", "Doe", 25))
        `when`(userService.getUsers(null)).thenReturn(Result.success(listUser))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/users")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun getUsers_WithAgeParameter_ReturnsUsers() {
        val listUser = listOf(User("john@example.com", "John", "Doe", 25))
        `when`(userService.getUsers(null)).thenReturn(Result.success(listUser))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/users")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(JsonUtils.toJson(listUser)))
    }

    @Test
    @Throws(Exception::class)
    fun createUser_ValidUser_ReturnsCreated() {
        val userCreationDTO = UserCreationDTO("john@example.com", "John", "Doe", 25)
        `when`(userService.createUser(userCreationDTO.asUser())).thenReturn(Result.success(userCreationDTO.asUser()))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/users")
                .header("apiKey", "cetteapikeyestvraimentpuissante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(userCreationDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun updateUser_ValidUser_ReturnsOk() {
        val userDTO = UserDTO("john@example.com", "John", "Doe", 25, mutableListOf())
        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/users/{email}", "john.doe@example.com")
                .header("apiKey", "cetteapikeyestvraimentpuissante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(userDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun deleteUser_ExistingEmail_ReturnsOk() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{email}", "john@example.com")
            .header("apiKey", "cetteapikeyestvraimentpuissante")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun getUserByEmail_ValidEmail_ReturnsUser() {
        val userDTO = UserDTO("john@example.com", "John", "Doe", 25, mutableListOf())
        `when`(userService.getUserByEmail("john@example.com")).thenReturn(Result.success(userDTO.asUser()))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{email}", "john@example.com")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(userDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(JsonUtils.toJson(userDTO)))
    }

    @Test
    @Throws(Exception::class)
    fun addFavoritesMovie_ValidRequest_ReturnsOk() {
        val addMovieDTO = AddMovieDTO(1)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/users/{email}/favoriteMovies", "john@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(addMovieDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun deletefavoriteMovie_ValidRequest_ReturnsOk() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/users/{email}/favoriteMovies/{movieId}",
                "john@example.com",
                123
            )
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun getMoviePreferenceNumber_ValidMovieId_ReturnsNumber() {
        val movieDTO = MovieDTO(123, "Titanic", "1950-2")
        val favoriteMovieDTO = FavoriteMovieDTO(123, "Titanic", "1950-2", 1)
        `when`(userService.getMoviePreferenceNumber(123)).thenReturn(Result.success(favoriteMovieDTO))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies/{movieId}", 123)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(movieDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(JsonUtils.toJson(favoriteMovieDTO)))
    }

    @Test
    @Throws(Exception::class)
    fun movieDeleted_ValidMovieId_ReturnsOk() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/movies/{movieId}", 123)
            .header("apiKey", "cetteapikeyestvraimentpuissante")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }
}

object JsonUtils {
    private val objectMapper = ObjectMapper()
    @Throws(JsonProcessingException::class)
    fun <T> toJson(`object`: T): String {
        return objectMapper.writeValueAsString(`object`)
    }
}