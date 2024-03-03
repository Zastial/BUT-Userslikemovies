package com.userslikemovies.springapi.service

import com.userslikemovies.springapi.Exceptions.InvalidAgeException
import com.userslikemovies.springapi.Exceptions.UserNotFoundException
import com.userslikemovies.springapi.controller.dto.FavoriteMovieDTO
import com.userslikemovies.springapi.domain.User
import com.userslikemovies.springapi.repository.IUserRepository
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository : IUserRepository) : IUserService {

    override fun getUsers(age : Int?) : Result<List<User>>  {
        return if (age != null) {
            if (age >= 15) {
                Result.success(userRepository.getUsers())
            } else {
                Result.failure(InvalidAgeException())
            }
        } else {
            Result.success(userRepository.getUsers())
        }
    }

    override fun getUserByEmail(email: String): Result<User> {
        val user = userRepository.getUserByEmail(email)
        if (user != null) {
            return Result.success(user)
        }
        return Result.failure(UserNotFoundException())
    }

    override fun createUser(user : User): Result<User> {
        val (_, error) = userRepository.createUser(user)
        if (error != null) {
            return Result.failure(error)
        }
        return Result.success(user)
    }

    override fun updateUser(email : String, user : User): Result<User> {
       val (_, error) = userRepository.updateUser(email, user)
        if (error != null) {
            return Result.failure(error)
        }
        return Result.success(user)
    }

    override fun deleteUser(email: String): Result<User> {
        val deletedUser = userRepository.deleteUser(email)
        if (deletedUser != null) {
            return Result.success(deletedUser)
        }
        return Result.failure(Exception("Error while deleting user"))
    }

    override fun addUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val (user, error) = userRepository.addUserFavoriteMovie(email, movieId)
        if (error != null) {
            return Result.failure(error)
        }
        return Result.success(user!!)
    }

    override fun removeUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val (user, error) = userRepository.removeUserFavoriteMovie(email, movieId)
        if (error != null) {
            return Result.failure(error)
        }
        return Result.success(user!!)
    }

    override fun movieDeleted(movieId: Int): Exception? {
        return userRepository.movieDeleted(movieId)
    }

    override fun getMoviePreferenceNumber(movieId: Int): Result<FavoriteMovieDTO> {
        val (movieNumber, error) = userRepository.getMoviePreferenceNumber(movieId)
        if (error != null) {
            return Result.failure(error)
        }
        return Result.success(movieNumber!!)
    }
}