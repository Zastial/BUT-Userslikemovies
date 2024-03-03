package com.userslikemovies.springapi.controller

import com.userslikemovies.springapi.Exceptions.*
import com.userslikemovies.springapi.config.CustomProperties
import com.userslikemovies.springapi.controller.dto.AddMovieDTO
import com.userslikemovies.springapi.controller.dto.UserCreationDTO
import com.userslikemovies.springapi.controller.dto.UserDTO
import com.userslikemovies.springapi.domain.User
import com.userslikemovies.springapi.error.FunctionnalError
import com.userslikemovies.springapi.repository.IUserRepository
import com.userslikemovies.springapi.service.IUserService
import com.userslikemovies.springapi.service.UserService
import jakarta.validation.constraints.Email
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry

@RestController
@Validated
class UserController(val userService: IUserService, private val customProperties : CustomProperties) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/api/v1/users")
    fun getUsers(@RequestParam age : Int?): ResponseEntity<out Any> {
        logger.info("Get all users")
        var result = userService.getUsers(age)
        if (result.isSuccess){
            if (result.getOrNull() != null){
                if (result.getOrNull()!!.size < 10){
                    return ResponseEntity.status(HttpStatus.OK).body(result)
                }else{
                    return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(result.getOrNull()!!.take(10))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FunctionnalError("400", "Bad Request"))
    }

    @PostMapping("api/v1/users")
    fun createUser(@RequestHeader apiKey : String, @RequestBody userDTO : UserCreationDTO): ResponseEntity<out Any> {
        logger.info("Received request to get users.")
        if (apiKey != customProperties.apiKey) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized").toString())
        }
        var result = userService.createUser(userDTO.asUser())
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserAlreadyExistsException){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(FunctionnalError("409", "User already exist").toString())
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FunctionnalError("400", "Bad Request").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @PutMapping("api/v1/users/{email}")
    fun updateUser(@RequestHeader apiKey : String, @PathVariable email : String, @RequestBody userDTO : UserDTO): ResponseEntity<out Any> {
        logger.info("Update user")
        if (apiKey != customProperties.apiKey){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized").toString())
        }
        var result = userService.updateUser(email, userDTO.asUser())
        if (result.isFailure){
            if (result.exceptionOrNull() != null) {
                if (result.exceptionOrNull() is UserNotFoundException) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }else if (result.exceptionOrNull() is InvalidPayload){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FunctionnalError("400", "Invalid payload").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/users/{email}")
    fun deleteUser(@RequestHeader apiKey : String, @PathVariable email : String): ResponseEntity<out Any> {
        logger.info("Delete user")
        if (apiKey != customProperties.apiKey) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized").toString())
        }
        val result = userService.deleteUser(email)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("api/v1/users/{email}")
    fun getUserByEmail(@PathVariable @Email email: String): ResponseEntity<out Any> {
        logger.info("Get user by email")
        val result = userService.getUserByEmail(email)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @PostMapping("api/v1/users/{email}/favoriteMovies")
    fun addFavoritesMovie(@PathVariable @Email email : String, @RequestBody addMovie : AddMovieDTO): ResponseEntity<out Any> {
        logger.info("Add a favorite movie of a user")
       var result = userService.addUserFavoriteMovie(email, addMovie.movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is MovieNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found").toString())
                }else if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }else if (result.exceptionOrNull() is MovieAlreadyInFavorites){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(FunctionnalError("409", "Movie already in favorites").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/users/{email}/favoriteMovies/{movieId}")
    fun deletefavoriteMovie(@PathVariable @Email email : String, @PathVariable movieId : Int): ResponseEntity<out Any> {
        logger.info("Delete a favorite movie of a user")
        var result = userService.removeUserFavoriteMovie(email, movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }else if (result.exceptionOrNull() is MovieNotInFavorites){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("400", "Movie not in favorites").toString())
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("api/v1/movies/{movieId}")
    fun getMoviePreferenceNumber(@PathVariable movieId : Int): ResponseEntity<out Any> {
        logger.info("Get the number of users who have bookmarked the movie")
        var result = userService.getMoviePreferenceNumber(movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is MovieNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/movies/{movieId}")
    fun movieDeleted(@RequestHeader apiKey : String, @PathVariable movieId : Int): ResponseEntity<out Any> {
        logger.info("Delete a movie")
        if (apiKey == customProperties.apiKey){
            var result = userService.movieDeleted(movieId)
            if (result ==  null){
                return ResponseEntity.status(HttpStatus.OK).body(result)
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found").toString())
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized").toString())
        }
    }
}