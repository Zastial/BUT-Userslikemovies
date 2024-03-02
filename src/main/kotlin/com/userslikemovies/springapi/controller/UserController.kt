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

@RestController
@Validated
class UserController(val userService: IUserService, private val customProperties : CustomProperties) {

    @GetMapping("/api/v1/users")
    fun getUsers(@RequestParam age : Int?): ResponseEntity<out Any> {
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

    //Verfier les exceptions conflits
    @PostMapping("api/v1/users")
    fun createUser(@RequestHeader apiKey : String, @RequestBody userDTO : UserCreationDTO): ResponseEntity<out Any> {
        if (apiKey != customProperties.apiKey) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized"))
        }
        var result = userService.createUser(userDTO.asUser())
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserAlreadyExistsException){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(FunctionnalError("409", "User already exist"))
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FunctionnalError("400", "Bad Request"))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @PutMapping("api/v1/users/{email}")
    fun updateUser(@RequestHeader apiKey : String, @PathVariable email : String, @RequestBody userDTO : UserDTO): ResponseEntity<out Any> {
        println(apiKey)
        if (apiKey != customProperties.apiKey){
            print("45")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized"))
        }
        var result = userService.updateUser(email, userDTO.asUser())
        if (result.isFailure){
            if (result.exceptionOrNull() != null) {
                if (result.exceptionOrNull() is UserNotFoundException) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found"))
                }else if (result.exceptionOrNull() is InvalidPayload){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FunctionnalError("400", "Invalid payload"))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/users/{email}")
    fun deleteUser(@RequestHeader apiKey : String, @PathVariable email : String): ResponseEntity<out Any> {
        if (apiKey != customProperties.apiKey) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized"))
        }
        val result = userService.deleteUser(email)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found"))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("api/v1/users/{email}")
    fun getUserByEmail(@PathVariable @Email email: String): ResponseEntity<out Any> {
        val result = userService.getUserByEmail(email)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found"))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @PostMapping("api/v1/users/{email}/favoriteMovies")
    fun addFavoritesMovie(@PathVariable @Email email : String, @RequestBody addMovie : AddMovieDTO): ResponseEntity<out Any> {
       var result = userService.addUserFavoriteMovie(email, addMovie.movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is MovieNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found"))
                }else if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found"))
                }else if (result.exceptionOrNull() is MovieAlreadyInFavorites){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(FunctionnalError("409", "Movie already in favorites"))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/users/{email}/favoriteMovies/{movieId}")
    fun deletefavoriteMovie(@PathVariable @Email email : String, @PathVariable movieId : Int): ResponseEntity<out Any> {
        var result = userService.removeUserFavoriteMovie(email, movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found"))
                }else if (result.exceptionOrNull() is MovieNotInFavorites){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("400", "Movie not in favorites"))
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found"))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("api/v1/movies/{movieId}")
    fun getMoviePreferenceNumber(@PathVariable movieId : Int): ResponseEntity<out Any> {
        var result = userService.getMoviePreferenceNumber(movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is MovieNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found"))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/movies/{movieId}")
    fun movieDeleted(@RequestHeader apiKey : String, @PathVariable movieId : Int): ResponseEntity<FunctionnalError> {
        if (apiKey == customProperties.apiKey){
            var result = userService.movieDeleted(movieId)
            if (result ==  null){
                return ResponseEntity.status(HttpStatus.OK).body(result)
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found"))
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized"))
        }
    }
}