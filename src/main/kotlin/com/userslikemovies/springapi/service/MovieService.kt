package com.userslikemovies.springapi.service
import com.userslikemovies.springapi.repository.IMovieRepository
import com.userslikemovies.springapi.repository.MovieRepository
import org.springframework.stereotype.Service

@Service
class MovieService(private val movieRepository : IMovieRepository) : IMovieService{
    override fun movieDeleted(movieId: Int) = movieRepository.movieDeleted(movieId)
    override fun getMoviePreferenceNumber(movieId: Int) = movieRepository.getMoviePreferenceNumber(movieId)
}