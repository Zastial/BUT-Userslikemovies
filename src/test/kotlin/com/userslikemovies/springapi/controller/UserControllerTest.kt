package com.userslikemovies.springapi.controller

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.userslikemovies.springapi.controller.dto.AddMovieDTO
import com.userslikemovies.springapi.controller.dto.MovieDTO
import com.userslikemovies.springapi.controller.dto.UserCreationDTO
import com.userslikemovies.springapi.controller.dto.UserDTO
import com.userslikemovies.springapi.service.IUserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.hamcrest.Matchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.hamcrest.Matchers.equalTo
import org.springframework.test.web.servlet.get

@WebMvcTest(UserController::class)
@ExtendWith(MockitoExtension::class)
internal class UserControllerTest {
    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val userService: IUserService? = null

    @Test
    @Throws(java.lang.Exception::class)
    fun getUsers_WithAgeParameter_Ok() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.get("/api/v1/users")
                .param("age", "25")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun getUsers_WithAgeParameter_ReturnsUsers() {
        val userCreationDTO = UserCreationDTO("john@example.com", "John", "Doe", 25)
        val userExpected = UserDTO("john@example.com", "John", "Doe", 25, mutableListOf())

        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(userCreationDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/users")
                .param("age", "25")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())

        mockMvc.get("/api/v1/users")
            .andExpect {
                status { isOk() }
                content { string("Hello World") }
            }
    }

    @Test
    @Throws(Exception::class)
    fun createUser_ValidUser_ReturnsCreated() {
        val userCreationDTO = UserCreationDTO("john@example.com", "John", "Doe", 25)
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(userCreationDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun updateUser_ValidUser_ReturnsOk() {
        val userDTO = UserDTO("john@example.com", "John", "Doe", 25, mutableListOf())
        mockMvc!!.perform(
            MockMvcRequestBuilders.put("/api/v1/users/{email}", "john.doe@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(userDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun deleteUser_ExistingEmail_ReturnsOk() {
        mockMvc!!.perform(MockMvcRequestBuilders.delete("/api/v1/users/{email}", "john@example.com"))
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun getUserByEmail_ValidEmail_ReturnsUser() {
        val userDTO = UserDTO("john@example.com", "John", "Doe", 25, mutableListOf())
        mockMvc!!.perform(MockMvcRequestBuilders.get("/api/v1/users/{email}", "john@example.com")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(userDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun addFavoritesMovie_ValidRequest_ReturnsOk() {
        val addMovieDTO = AddMovieDTO(1)
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/v1/users/{email}/favoriteMovies", "john@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(addMovieDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun deletefavoriteMovie_ValidRequest_ReturnsOk() {
        mockMvc!!.perform(
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
        mockMvc!!.perform(MockMvcRequestBuilders.get("/api/v1/movies/{movieId}", 123)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(movieDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun movieDeleted_ValidMovieId_ReturnsOk() {
        mockMvc!!.perform(MockMvcRequestBuilders.delete("/api/v1/movies/{movieId}", 123))
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