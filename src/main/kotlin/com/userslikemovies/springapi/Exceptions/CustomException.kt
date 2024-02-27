package com.userslikemovies.springapi.Exceptions

class UserNotFoundException() : Exception("User not found")

class UserAlreadyExistsException() : Exception("User already exists")

class MovieNotFoundException() : Exception("Movie not found")

class InvalidMovieIdException() : Exception("Invalid movieId : Id must be greater than 0")

class InvalidAgeException() : Exception("Invalid age : Age must be greater than 15")