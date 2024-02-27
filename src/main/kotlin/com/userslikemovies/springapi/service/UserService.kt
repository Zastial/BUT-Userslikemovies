package com.userslikemovies.springapi.service

import com.userslikemovies.springapi.domain.User
import com.userslikemovies.springapi.repository.IUserRepository
import com.userslikemovies.springapi.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository : IUserRepository) : IUserService {
    override fun getUsers() = userRepository.getUsers()
    override fun getUserByEmail(email: String) = userRepository.getUserByEmail(email)
    override fun createUser(user : User) = userRepository.createUser(user)
    override fun updateUser(email : String, user : User) = userRepository.updateUser(email, user)
    override fun deleteUser(email: String) = userRepository.deleteUser(email)
    override fun addUserFavoriteMovie(email: String, movieId: Int) = userRepository.addUserFavoriteMovie(email, movieId)
    override fun removeUserFavoriteMovie(email: String, movieId: Int) = userRepository.removeUserFavoriteMovie(email, movieId)
    override fun movieDeleted(movieId: Int) = userRepository.movieDeleted(movieId)
    override fun getMoviePreferenceNumber(movieId: Int) = userRepository.getMoviePreferenceNumber(movieId)
}