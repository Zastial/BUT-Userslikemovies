package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.config.CustomProperties
import com.userslikemovies.springapi.config.PropertiesConfig
import com.userslikemovies.springapi.controller.dto.UserDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.getForEntity

@Repository
class MovieRepository(val jpa : JpaRepositoryMovie, val customProperties : CustomProperties) : IMovieRepository {

    val restTemplate = RestTemplateBuilder().rootUri(customProperties.baseurl).build()

    //val result: ResponseEntity<List<UserDTO>> = restTemplate.getForEntity("/api/users")
    override fun movieDeleted(movieId: Int): Result<Unit> {
        return try {
            jpa.deleteById(movieId.toString())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMoviePreferenceNumber(movieId: Int): Result<Int> {
        val movie = jpa.findById(movieId.toString())
        return if (movie.isPresent) {
            Result.success(movie.get().id)
        } else {
            Result.failure(Exception("Movie not found"))
        }
    }
}