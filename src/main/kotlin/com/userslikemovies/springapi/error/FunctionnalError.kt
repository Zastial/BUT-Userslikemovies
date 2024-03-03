package com.userslikemovies.springapi.error

class FunctionnalError (status : String, message : String) {
    val status = status
    val message = message

    override fun toString(): String {
        return "$status - $message"
    }
}