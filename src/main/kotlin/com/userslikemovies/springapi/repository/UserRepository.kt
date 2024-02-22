package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.domain.User
import org.springframework.stereotype.Repository

@Repository
class UserRepository : IUserRepository {
    override fun getUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override fun getUserByEmail(email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun createUser(user: User): Result<User> {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User): Result<User> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun addUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        TODO("Not yet implemented")
    }

    override fun removeUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        TODO("Not yet implemented")
    }

    override fun movieDeleted(movieId: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getMoviePreferenceNumber(movieId: Int): Result<Int> {
        TODO("Not yet implemented")
    }
}