package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.config.CustomProperties
import com.userslikemovies.springapi.domain.Movie
import com.userslikemovies.springapi.domain.User
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.util.*

@Repository
class UserRepository(val jpa : JpaRepositoryUser, private final val customProperties : CustomProperties) : IUserRepository {

    val restTemplate: RestTemplate = RestTemplateBuilder().rootUri(customProperties.baseurl).build()

    override fun getUsers(): List<User> {
        return jpa.findAll()
    }

    override fun getUserByEmail(email: String): User? {
        return jpa.findByIdOrNull(email)

    }

    override fun createUser(user: User): User? {
        val doesUserAlreadyExists = getUserByEmail(user.email)
        if (doesUserAlreadyExists != null) {
            return null
        }
        return jpa.save(user)
    }

    override fun updateUser(email : String, user : User): User? {
        getUserByEmail(email) ?: return null

        val userByEmail = jpa.findById(email).get()
        userByEmail.firstName = user.firstName
        userByEmail.lastName = user.lastName
        userByEmail.age = user.age
        userByEmail.movies = user.movies

        return try {
            jpa.save(userByEmail)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteUser(email: String): User? {
        val user = jpa.findById(email)
        return if (user.isPresent) {
            jpa.delete(user.get())
            user.get()
        } else {
            null
        }
    }

    override fun addUserFavoriteMovie(email: String, movieId: Int): User? {
        val user = jpa.findById(email)
        val movie : ResponseEntity<Movie> = restTemplate.getForEntity("/movies/${movieId}")

        if (movie.body!!.name.isNotEmpty()) {
            return if (user.isPresent) {
                user.get().movies.plus(movie.body)
                jpa.save(user.get())
                user.get()
            } else {
                null
            }
        }
        return null
    }

    override fun removeUserFavoriteMovie(email: String, movieId: Int): User? {
        val user = jpa.findById(email)
        val movie : ResponseEntity<Movie> = restTemplate.getForEntity("/movies/${movieId}")

        if (movie.body!!.name.isNotEmpty()) {
            return if (user.isPresent) {
                user.get().movies.minus(movie.body)
                jpa.save(user.get())
                user.get()
            } else {
                null
            }
        }
        return null
    }

    override fun movieDeleted(movieId: Int): Result<Unit> {
        val users = jpa.findAll()
        val movieById: ResponseEntity<Movie> = restTemplate.getForEntity("/movies/${movieId}")

        if (movieById.body != null) {
            users.forEach {
                it.movies.minus(movieById)
            }
            return Result.success(Unit)
        }
        return Result.failure(Exception("No movie found"))
    }

    override fun getMoviePreferenceNumber(movieId: Int): Int? {
        val users = jpa.findAll()
        val movieById: ResponseEntity<Movie> = restTemplate.getForEntity("/movies/${movieId}")

        var result = 0
        if (movieById.body != null) {
            users.forEach {user ->
                user.movies.forEach {
                    if (it!!.id == movieId) {
                        result++
                    }
                }
            }
            return result
        }
        return null
    }
}