package com.example.demo.movie.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.movie.model.MovieList;
import com.example.demo.movie.repo.MovieDetailRepository;
import com.example.demo.movie.repo.MovieListRepository;

@Service
public class MovieListService {

	@Autowired
	private MovieListRepository movieListRepository;
	
	@Autowired
	private MovieDetailRepository movieDetailRepository;

	public List<MovieList> findAll() {
		return movieListRepository.findAll();
	}

	public Optional<MovieList> findById(Integer id) {
		return movieListRepository.findById(id);

	}

	public MovieList updateMovie(Integer id, MovieList movieDetails) {
		Optional<MovieList> existingMovie = movieListRepository.findById(id);
		if (existingMovie.isPresent()) {
			MovieList movie = existingMovie.get();
			movie.setName(movieDetails.getName());
			movie.setReleased(movieDetails.getReleased());
			movie.setRetired(movieDetails.getRetired());
			movie.setRuntime(movieDetails.getRuntime());
			movie.setState(movieDetails.getState());
			return movieListRepository.save(movie); // 儲存更新後的電影
		}
		return null;
		
	}
	public boolean deleteById(Integer id) {
        if (movieListRepository.existsById(id)) {
        	movieDetailRepository.deleteById(id);
            movieListRepository.deleteById(id);
            return true;  // 刪除成功
        }
        return false;  // 如果找不到電影，返回false
    }
	
	public MovieList save(MovieList movie) {
	    return movieListRepository.save(movie);
	}
	
	public List<MovieList> getAvailableMovies() {
	    return movieListRepository.findByState("上映"); // 
	}
}
