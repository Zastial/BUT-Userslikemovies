package com.userslikemovies.springapi

import com.userslikemovies.springapi.controller.JsonUtils
import com.userslikemovies.springapi.controller.dto.AddMovieDTO
import com.userslikemovies.springapi.controller.dto.UserCreationDTO
import com.userslikemovies.springapi.controller.dto.UserDTO
import com.userslikemovies.springapi.domain.Movie
import com.userslikemovies.springapi.domain.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserLikeMoviesApplicationTests {

	@LocalServerPort
	private val port: Int = 8080

	@Autowired
	private lateinit var restTemplate: TestRestTemplate

	private lateinit var headers: HttpHeaders
	@BeforeEach
	fun setup() {
		headers = HttpHeaders()
		headers.accept = listOf(MediaType.APPLICATION_JSON)
		headers.contentType = MediaType.APPLICATION_JSON
		headers.set("apiKey", "cetteapikeyestvraimentpuissante")
	}

	@Test
	fun endToEnd() {
		val url = "http://localhost:$port/api/v1/users"
		val response: ResponseEntity<String> = restTemplate.getForEntity(url, String::class.java)

		assertEquals("[]", response.body)
		assertEquals(HttpStatus.OK, response.statusCode)
	}

	@Test
	fun endToEnd_create_user() {
		val url = "http://localhost:$port/api/v1/users"
		val userDTO = UserCreationDTO("john@example.com", "John", "Doe", 25)
		val expectedUser = UserDTO("john@example.com", "John", "Doe", 25, mutableListOf())

		val entity = HttpEntity(JsonUtils.toJson(userDTO), headers)
		val response: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)

		assertEquals(HttpStatus.OK, response.statusCode)
		assertEquals(JsonUtils.toJson(expectedUser), response.body)
	}

	@Test
	fun endToEnd_add_movie() {
		// Add user
		var url = "http://localhost:$port/api/v1/users"
		val userDTO = UserCreationDTO("john@example.com", "John", "Doe", 25)
		var entity = HttpEntity(JsonUtils.toJson(userDTO), headers)
		restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)

		// Add movie to user
		url = "http://localhost:$port/api/v1/users/john@example.com/favoriteMovies"
		val addMovieDTO = AddMovieDTO(1)
		entity = HttpEntity(JsonUtils.toJson(addMovieDTO), headers)
		var response: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)


		// Check if movie was added
		val expectedUserMovie = User("john@example.com", "John", "Doe", 25, mutableListOf(Movie(1, "Titanic", "1950-2")))
		assertEquals(HttpStatus.OK, response.statusCode)
		assertEquals(JsonUtils.toJson(expectedUserMovie), response.body)

		// Delete user
		url = "http://localhost:$port/api/v1/users/john@example.com"
		entity = HttpEntity(JsonUtils.toJson(userDTO), headers)
		restTemplate.exchange(url, HttpMethod.DELETE, entity, String::class.java)
	}
}