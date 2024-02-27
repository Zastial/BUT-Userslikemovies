package com.userslikemovies.springapi.service

import com.userslikemovies.springapi.controller.dto.FavoriteMovieDTO
import com.userslikemovies.springapi.domain.User

interface IUserService {
    fun getUsers(age : Int?) : Result<List<User>>
    fun getUserByEmail(email : String): Result<User>
    fun createUser(user : User) : Result<User>
    fun updateUser(email : String, user : User) : Result<User>
    fun deleteUser(email : String) : Result<User>
    fun addUserFavoriteMovie(email : String, movieId : Int) : Result<User>
    fun removeUserFavoriteMovie(email : String, movieId : Int) : Result<User>
    fun movieDeleted(movieId: Int) : Exception?
    fun getMoviePreferenceNumber(movieId: Int) : Result<FavoriteMovieDTO>
}