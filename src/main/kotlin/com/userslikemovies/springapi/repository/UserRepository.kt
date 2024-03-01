package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.Exceptions.InvalidMovieIdException
import com.userslikemovies.springapi.Exceptions.MovieNotFoundException
import com.userslikemovies.springapi.Exceptions.UserAlreadyExistsException
import com.userslikemovies.springapi.Exceptions.UserNotFoundException
import com.userslikemovies.springapi.config.CustomProperties
import com.userslikemovies.springapi.controller.dto.FavoriteMovieDTO
import com.userslikemovies.springapi.domain.Movie
import com.userslikemovies.springapi.domain.MovieAPI
import com.userslikemovies.springapi.domain.User
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.util.*

@Repository
class UserRepository(val jpa : JpaRepositoryUser, private val customProperties : CustomProperties) : IUserRepository {

    val restTemplate: RestTemplate = RestTemplateBuilder().rootUri(customProperties.baseurl).build()

    override fun getUsers(): List<User> {
        return jpa.findAll()
    }

    override fun getUserByEmail(email: String): User? {
        return jpa.findByIdOrNull(email)
    }

    override fun createUser(user: User): Pair<User?, Exception?>{
        val doesUserAlreadyExists = getUserByEmail(user.email)
        if (doesUserAlreadyExists != null) {
            return Pair(null, UserAlreadyExistsException())
        }
        return try {
            Pair(jpa.save(user), null)
        } catch (e: Exception) {
            Pair(null, e)
        }
    }

    override fun updateUser(email : String, user : User): Pair<User?, Exception?> {
        getUserByEmail(email) ?: return Pair(null, UserNotFoundException())

        val userByEmail = jpa.findById(email).get()
        userByEmail.firstName = user.firstName
        userByEmail.lastName = user.lastName
        userByEmail.age = user.age
        userByEmail.favoriteMovies = user.favoriteMovies

        return try {
            Pair(jpa.save(userByEmail), null)
        } catch (e: Exception) {
            Pair(null, e)
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

    override fun addUserFavoriteMovie(email: String, movieId: Int): Pair<User?, Exception?> {
        if (movieId < 0) {
            return Pair(null, InvalidMovieIdException())
        }

        val user = jpa.findById(email)
        val movie : ResponseEntity<MovieAPI> = restTemplate.getForEntity("/api/v1/movies/${movieId}")

        if (movie.body!!.name.isNotEmpty()) {
            return if (user.isPresent) {
                val userDomain = user.get()
                userDomain.favoriteMovies.add(movie.body!!.toMovie())
                jpa.save(userDomain)
                Pair(userDomain, null)
            } else {
                Pair(null, UserNotFoundException())
            }
        }
        return Pair(null, MovieNotFoundException())
    }

    override fun removeUserFavoriteMovie(email: String, movieId: Int): Pair<User?, Exception?> {
        if (movieId < 0) {
            return Pair(null, InvalidMovieIdException())
        }

        val user = jpa.findById(email)
        val movie : ResponseEntity<MovieAPI> = restTemplate.getForEntity("/api/v1/movies/${movieId}")

        if (movie.body!!.name.isNotEmpty()) {
            return if (user.isPresent) {
                user.get().favoriteMovies.removeIf { it!!.id == movieId }
                jpa.save(user.get())
                Pair(user.get(), null)
            } else {
                Pair(null, UserNotFoundException())
            }
        }
        return Pair(null, MovieNotFoundException())
    }

    override fun movieDeleted(movieId: Int): Exception? {
        if (movieId < 0) {
            return InvalidMovieIdException()
        }

        val users = jpa.findAll()
        val movieById: ResponseEntity<MovieAPI> = restTemplate.getForEntity("/api/v1/movies/${movieId}")

        if (movieById.body != null) {
            users.forEach {user ->
                user.favoriteMovies.removeIf { it!!.id == movieId }
                jpa.save(user)
            }
            return null
        }
        return MovieNotFoundException()
    }

    override fun getMoviePreferenceNumber(movieId: Int): Pair<FavoriteMovieDTO?, Exception?> {
        if (movieId < 0) {
            return Pair(null, InvalidMovieIdException())
        }

        val users = jpa.findAll()
        val movieById: ResponseEntity<MovieAPI> = restTemplate.getForEntity("/api/v1/movies/${movieId}")

        var result = 0
        if (movieById.body != null) {
            users.forEach {user ->
                user.favoriteMovies.forEach {
                    if (it!!.id == movieId) {
                        result++
                    }
                }
            }

            val releaseDate = movieById.body!!.year.toString() + "-" + movieById.body!!.month.toString()
            val favoriteMovieDTO = FavoriteMovieDTO(movieById.body!!.id, movieById.body!!.name, releaseDate, result)
            return Pair(favoriteMovieDTO, null)
        }
        return Pair(null, MovieNotFoundException())
    }
}