package com.userslikemovies.springapi.service

import com.userslikemovies.springapi.domain.User

interface IUserService {
    fun getUsers() : List<User>
    fun getUserByEmail(email : String) : Result<User>
    fun createUser(user : User) : Result<User>
    fun updateUser(user : User) : Result<User>
    fun deleteUser(email : String) : Result<User>
    fun addUserFavoriteMovie(email : String, movieId : Int) : Result<User>
    fun removeUserFavoriteMovie(email : String, movieId : Int) : Result<User>
    fun movieDeleted(movieId : Int) : Result<Unit>
    fun getMoviePreferenceNumber(movieId : Int) : Result<Int>
}