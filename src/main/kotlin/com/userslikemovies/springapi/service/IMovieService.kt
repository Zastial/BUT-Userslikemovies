package com.userslikemovies.springapi.service

interface IMovieService {
    fun movieDeleted(movieId: Int) : Result<Unit>
    fun getMoviePreferenceNumber(movieId: Int) : Result<Int>
}