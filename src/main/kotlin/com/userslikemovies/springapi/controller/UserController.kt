package com.userslikemovies.springapi.controller

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
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class UserController(val userService: IUserService) {

    @GetMapping("/api/v1/users")
    fun getUsers() = userService.getUsers()

    @PostMapping("api/v1/users")
    fun createUser(@RequestBody userDTO : UserDTO) {
        userService.createUser(userDTO.asUser())
    }

    @PutMapping("api/v1/users")
    fun updateUser(@RequestBody userDTO : UserDTO) {
        userService.updateUser(userDTO.asUser())
    }

    @DeleteMapping("api/v1/users/{email}")
    fun deleteUser(@PathVariable email : String) {
        userService.deleteUser(email)
    }

    @GetMapping("api/v1/users/{email}")
    fun getUserByEmail(@PathVariable @Email email: String) = userService.getUserByEmail(email)

    @PostMapping("api/v1/users/{email}/favoritesMovies")
    fun addFavoritesMovie(@PathVariable @Email email : String, @RequestBody movieId : Int) {
        userService.addUserFavoriteMovie(email, movieId)
    }

    @DeleteMapping("api/v1/users/{email}/favoritesMovies/{movieId}")
    fun deletefavoriteMovie(@PathVariable @Email email : String, @PathVariable movieId : Int) {
        userService.removeUserFavoriteMovie(email, movieId)
    }
}