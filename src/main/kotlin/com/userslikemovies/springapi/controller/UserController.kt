package com.userslikemovies.springapi.controller

import com.userslikemovies.springapi.controller.dto.AddMovieDTO
import com.userslikemovies.springapi.controller.dto.UserCreationDTO
import com.userslikemovies.springapi.controller.dto.UserDTO
import com.userslikemovies.springapi.repository.IUserRepository
import com.userslikemovies.springapi.service.IUserService
import com.userslikemovies.springapi.service.UserService
import jakarta.validation.constraints.Email
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class UserController(val userService: IUserService) {

    @GetMapping("/api/v1/users")
    fun getUsers(@RequestParam age : Int?) = userService.getUsers(age) // Gérer si users > 10

    @PostMapping("api/v1/users")
    fun createUser(@RequestBody userDTO : UserCreationDTO) {
        userService.createUser(userDTO.asUser())
    }

    @PutMapping("api/v1/users/{email}")
    fun updateUser(@PathVariable email : String, @RequestBody userDTO : UserDTO) {
        userService.updateUser(email, userDTO.asUser())
    }

    @DeleteMapping("api/v1/users/{email}")
    fun deleteUser(@PathVariable email : String) {
        val result = userService.deleteUser(email)
    }

    @GetMapping("api/v1/users/{email}")
    fun getUserByEmail(@PathVariable @Email email: String) = userService.getUserByEmail(email)

    @PostMapping("api/v1/users/{email}/favoriteMovies")
    fun addFavoritesMovie(@PathVariable @Email email : String, @RequestBody addMovie : AddMovieDTO) {
       userService.addUserFavoriteMovie(email, addMovie.movieId)
    }

    @DeleteMapping("api/v1/users/{email}/favoriteMovies/{movieId}")
    fun deletefavoriteMovie(@PathVariable @Email email : String, @PathVariable movieId : Int) {
        userService.removeUserFavoriteMovie(email, movieId)
    }

    @GetMapping("api/v1/movies/{movieId}")
    fun getMoviePreferenceNumber(@PathVariable movieId : Int) = userService.getMoviePreferenceNumber(movieId)

    @DeleteMapping("api/v1/movies/{movieId}")
    fun movieDeleted(@PathVariable movieId : Int) = userService.movieDeleted(movieId)
}