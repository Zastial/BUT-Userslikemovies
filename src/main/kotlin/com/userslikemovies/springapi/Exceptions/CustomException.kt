package com.userslikemovies.springapi.Exceptions

class UserNotFoundException() : Exception()

class UserAlreadyExistsException() : Exception("User already exists")

class MovieNotFoundException() : Exception("Movie not found")

class InvalidAgeException() : Exception("Invalid age : Age must be greater than 15")

class MovieNotInFavorites() : Exception("Movie not in favorites")

class InvalidPayload() : Exception("Invalid Payload")

class MovieAlreadyInFavorites() : Exception("Movie already in favorites")

