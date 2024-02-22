package com.userslikemovies.springapi.repository

import com.userslikemovies.springapi.domain.User
import org.springframework.stereotype.Repository

@Repository
class UserRepository(val jpa : JpaRepositoryUser) : IUserRepository {

    override fun getUsers(): List<User> {
        return jpa.findAll()
    }

    override fun getUserByEmail(email: String): Result<User> {
        val user = jpa.findById(email)
        return if (user.isPresent) {
            Result.success(user.get())
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    override fun createUser(user: User): Result<User> {
        return try {
            Result.success(jpa.save(user))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun updateUser(user: User): Result<User> {
        return try {
            Result.success(jpa.save(user))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun deleteUser(email: String): Result<User> {
        val user = jpa.findById(email)
        return if (user.isPresent) {
            jpa.delete(user.get())
            Result.success(user.get())
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    override fun addUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val user = jpa.findById(email)
        return if (user.isPresent) {
            user.get().moviesId.plus(movieId)
            jpa.save(user.get())
            Result.success(user.get())
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    override fun removeUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val user = jpa.findById(email)
        return if (user.isPresent) {
            user.get().moviesId.minus(movieId)
            jpa.save(user.get())
            Result.success(user.get())
        } else {
            Result.failure(Exception("User not found"))
        }
    }
}