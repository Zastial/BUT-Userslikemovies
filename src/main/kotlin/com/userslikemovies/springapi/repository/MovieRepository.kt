package com.userslikemovies.springapi.repository

import org.springframework.stereotype.Repository

@Repository
class MovieRepository(val jpa : JpaRepositoryMovie) : IMovieRepository {

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