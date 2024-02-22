package com.userslikemovies.springapi.repository

interface IMovieRepository {
    fun movieDeleted(movieId: Int): Result<Unit>
    fun getMoviePreferenceNumber(movieId: Int): Result<Int>
}