package com.userslikemovies.springapi.controller

import com.userslikemovies.springapi.controller.dto.UserDTO
import com.userslikemovies.springapi.repository.IUserRepository
import com.userslikemovies.springapi.service.IUserService
import com.userslikemovies.springapi.service.UserService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class UserController(val userService: IUserService) {

    @GetMapping("/users")
    fun getUsers() = userService.getUsers()

    @GetMapping("/users/{email}")
    fun getUserByEmail(email: String) = userService.getUserByEmail(email)

    @PostMapping("/users")
    fun createUser(@RequestBody userDTO : UserDTO) {
        userService.createUser(userDTO.asUser())
    }

}