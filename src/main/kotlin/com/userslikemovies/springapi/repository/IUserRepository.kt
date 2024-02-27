package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.domain.User
import java.util.*

interface IUserRepository {
    fun getUsers() : List<User>
    fun getUserByEmail(email : String) : User?
    fun createUser(user : User) : User?
    fun updateUser(email: String, user : User) : User?
    fun deleteUser(email : String) : User?
    fun addUserFavoriteMovie(email : String, movieId : Int) : User?
    fun removeUserFavoriteMovie(email : String, movieId : Int) : User?
    fun movieDeleted(movieId: Int): Result<Unit>
    fun getMoviePreferenceNumber(movieId: Int): Int?
}