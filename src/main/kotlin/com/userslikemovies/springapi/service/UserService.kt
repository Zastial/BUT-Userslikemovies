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
class UserService(private val userRepository : IUserRepository, private val meterRegistry: MeterRegistry) : IUserService {

    val http2xxCounter: Counter = Counter.builder("call.movieservice.2xx")
        .tag("status", "2xx")
        .description("Number of HTTP 2xx responses")
        .register(meterRegistry)

    val http4xxCounter: Counter = Counter.builder("call.movieservice.4xx")
        .tag("status", "4xx")
        .description("Number of HTTP 4xx responses")
        .register(meterRegistry)

    val http5xxCounter: Counter = Counter.builder("call.movieservice.5xx")
        .tag("status", "5xx")
        .description("Number of HTTP 5xx responses")
        .register(meterRegistry)

    override fun getUsers(age : Int?) : Result<List<User>>  {
        return if (age != null) {
            if (age >= 15) {
                http2xxCounter.increment()
                Result.success(userRepository.getUsers())
            } else {
                http4xxCounter.increment()
                Result.failure(InvalidAgeException())
            }
        } else {
            http2xxCounter.increment()
            Result.success(userRepository.getUsers())
        }
    }

    override fun getUserByEmail(email: String): Result<User> {
        val (user, _) = userRepository.getUserByEmail(email)
        if (user != null) {
            http2xxCounter.increment()
            return Result.success(user)
        }
        http4xxCounter.increment()
        return Result.failure(UserNotFoundException())
    }

    override fun createUser(user : User): Result<User> {
        val (_, error) = userRepository.createUser(user)
        if (error != null) {
            http4xxCounter.increment()
            return Result.failure(error)
        }
        http2xxCounter.increment()
        return Result.success(user)
    }

    override fun updateUser(email : String, user : User): Result<User> {
       val (_, error) = userRepository.updateUser(email, user)
        if (error != null) {
            http4xxCounter.increment()
            return Result.failure(error)
        }
        http2xxCounter.increment()
        return Result.success(user)
    }

    override fun deleteUser(email: String): Result<User> {
        val (deletedUser, error) = userRepository.deleteUser(email)
        if (error != null) {
            http4xxCounter.increment()
            return Result.failure(error)
        }
        http2xxCounter.increment()
        return Result.success(deletedUser!!)
    }

    override fun addUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val (user, error) = userRepository.addUserFavoriteMovie(email, movieId)
        if (error != null) {
            http4xxCounter.increment()
            return Result.failure(error)
        }
        http2xxCounter.increment()
        return Result.success(user!!)
    }

    override fun removeUserFavoriteMovie(email: String, movieId: Int): Result<User> {
        val (user, error) = userRepository.removeUserFavoriteMovie(email, movieId)
        if (error != null) {
            http4xxCounter.increment()
            return Result.failure(error)
        }
        http2xxCounter.increment()
        return Result.success(user!!)
    }

    override fun movieDeleted(movieId: Int): Exception? {
        val error = userRepository.movieDeleted(movieId)
        if (error != null) {
            http4xxCounter.increment()
            return error
        }
        http2xxCounter.increment()
        return null
    }

    override fun getMoviePreferenceNumber(movieId: Int): Result<FavoriteMovieDTO> {
        val (movieNumber, error) = userRepository.getMoviePreferenceNumber(movieId)
        if (error != null) {
            http4xxCounter.increment()
            return Result.failure(error)
        }
        http2xxCounter.increment()
        return Result.success(movieNumber!!)
    }
}