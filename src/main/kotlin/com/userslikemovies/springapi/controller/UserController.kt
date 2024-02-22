package com.userslikemovies.springapi.controller

import com.userslikemovies.springapi.repository.IUserRepository
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class UserController(val userRepository: IUserRepository) {

}