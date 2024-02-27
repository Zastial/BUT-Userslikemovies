package com.userslikemovies.springapi.controller

import com.userslikemovies.springapi.service.IMovieService
import com.userslikemovies.springapi.service.IUserService
import com.userslikemovies.springapi.service.MovieService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class MovieController (val movieService: IMovieService){
    @GetMapping("/api/v1/movies/{movieId}")
    fun getMoviePreferenceNumber(@PathVariable movieId : Int) = movieService.getMoviePreferenceNumber(movieId)

    @DeleteMapping("/api/v1/movies/{movieId}")
    fun movieDeleted(@PathVariable movieId : Int) = movieService.movieDeleted(movieId)

}