package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.config.CustomProperties
import com.userslikemovies.springapi.domain.Movie
import com.userslikemovies.springapi.domain.User
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Repository
class UserRepository(val jpa : JpaRepositoryUser, private final val customProperties : CustomProperties) : IUserRepository {

    val restTemplate: RestTemplate = RestTemplateBuilder().rootUri(customProperties.baseurl).build()

    override fun getUsers(): Result<List<User>> {
        return Result.success(jpa.findAll())
    }

    override fun getUserByEmail(email: String): Result<User> {
        val user = jpa.findById(email)
        return if (user.isPresent) {
            Result.success(user.get())
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    override fun createUser(user: User): Result<User> {
        val doesUserAlreadyExists = getUserByEmail(user.email)
        return if (doesUserAlreadyExists.isSuccess) {
            try {
                Result.success(jpa.save(user))
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("User already exists"))
        }
    }

    override fun updateUser(email : String, user : User): Result<User> {
        val userByEmail = jpa.findById(email).get()

        if (email != user.email) {
            jpa.delete(userByEmail)
            jpa.save(user)
            return Result.success(user)
        } else {
            userByEmail.firstName = user.firstName
            userByEmail.lastName = user.lastName
            userByEmail.age = user.age
            userByEmail.movies = user.movies

            return try {
                Result.success(jpa.save(userByEmail))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun deleteUser(email: String): Result<User> {
        val user = jpa.findById(email)
        return if (user.isPresent) {
            jpa.delete(user.get())
            Result.success(user.get())
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    override fun addUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val user = jpa.findById(email)
        val movie : ResponseEntity<Movie> = restTemplate.getForEntity("/movies/${movieId}")

        if (movie.body!!.name.isNotEmpty()) {
            return if (user.isPresent) {
                user.get().movies.plus(movie.body)
                jpa.save(user.get())
                Result.success(user.get())
            } else {
                Result.failure(Exception("User not found"))
            }
        }
        return Result.failure(Exception("Movie not found"))
    }

    override fun removeUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val user = jpa.findById(email)
        val movie : ResponseEntity<Movie> = restTemplate.getForEntity("/movies/${movieId}")

        if (movie.body!!.name.isNotEmpty()) {
            return if (user.isPresent) {
                user.get().movies.minus(movie.body)
                jpa.save(user.get())
                Result.success(user.get())
            } else {
                Result.failure(Exception("User not found"))
            }
        }
        return Result.failure(Exception("Movie not found"))
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

    override fun getMoviePreferenceNumber(movieId: Int): Result<Int> {
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
            return Result.success(result)
        }
        return Result.failure(Exception("No movie found"))
    }
}