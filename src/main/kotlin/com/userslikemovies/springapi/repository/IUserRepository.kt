package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.controller.dto.FavoriteMovieDTO
import com.userslikemovies.springapi.domain.User
import java.util.*

interface IUserRepository {
    fun getUsers() : List<User>
    fun getUserByEmail(email : String) : User?
    fun createUser(user : User) : Pair<User?, Exception?>
    fun updateUser(email: String, user : User) : Pair<User?, Exception?>
    fun deleteUser(email : String) : User?
    fun addUserFavoriteMovie(email : String, movieId : Int) : Pair<User?, Exception?>
    fun removeUserFavoriteMovie(email : String, movieId : Int) : Pair<User?, Exception?>
    fun movieDeleted(movieId: Int): Exception?
    fun getMoviePreferenceNumber(movieId: Int): Pair<FavoriteMovieDTO?, Exception?>
}