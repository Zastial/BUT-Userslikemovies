package com.userslikemovies.springapi.service

import com.userslikemovies.springapi.domain.User
import com.userslikemovies.springapi.repository.IUserRepository
import com.userslikemovies.springapi.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository : IUserRepository) : IUserService {
    override fun getUsers() : Result<List<User>>  {
        return Result.success(userRepository.getUsers())
    }

    override fun getUserByEmail(email: String): Result<User> {
        val user = userRepository.getUserByEmail(email)
        if (user != null) {
            return Result.success(user)
        }
        return Result.failure(Exception("User not found"))
    }

    override fun createUser(user : User): Result<User> {
        val createdUser = userRepository.createUser(user)
        if (createdUser != null) {
            return Result.success(user)
        }
        return Result.failure(Exception("User already exists"))
    }

    override fun updateUser(email : String, user : User): Result<User> {
       val updatedUser = userRepository.updateUser(email, user)
        if (updatedUser != null) {
            return Result.success(user)
        }
        return Result.failure(Exception("Error while saving updates"))
    }

    override fun deleteUser(email: String): Result<User> {
        val deletedUser = userRepository.deleteUser(email)
        if (deletedUser != null) {
            return Result.success(deletedUser)
        }
        return Result.failure(Exception("Error while deleting user"))
    }

    override fun addUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val user = userRepository.addUserFavoriteMovie(email, movieId)
        if (user != null) {
            return Result.success(user)
        }
        return Result.failure(Exception("User not found")) // Add Custom error
    }

    override fun removeUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val user = userRepository.removeUserFavoriteMovie(email, movieId)
        if (user != null) {
            return Result.success(user)
        }
        return Result.failure(Exception("User not found")) // Add Custom error
    }

    override fun movieDeleted(movieId: Int): Result<Unit> {
        return userRepository.movieDeleted(movieId)
    }

    override fun getMoviePreferenceNumber(movieId: Int): Result<Int> {
        val movieNumber = userRepository.getMoviePreferenceNumber(movieId)
        if (movieNumber != null) {
            return Result.success(movieNumber)
        }
        return Result.failure(Exception("This movie isn't liked very much")) // Add Custom error
    }
}