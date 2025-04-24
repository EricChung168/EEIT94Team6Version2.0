package com.example.demo.movie.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.movie.model.MovieList;
import com.example.demo.movie.model.MovieShowtime;
import com.example.demo.movie.model.MovieTheater;
import com.example.demo.movie.repo.MovieListRepository;
import com.example.demo.movie.repo.MovieShowtimeRepository;
import com.example.demo.movie.repo.MovieTheaterRepository;

@Service
public class MovieShowtimeService {

	 @Autowired
	    private MovieListRepository movieListRepo;

	    @Autowired
	    private MovieTheaterRepository theaterRepo;

	    @Autowired
	    private MovieShowtimeRepository showtimeRepo;

	    public void addShowtime(Integer movieId, Integer theaterId, LocalDate date, LocalTime time) {
	        MovieList movie = movieListRepo.findById(movieId)
	                .orElseThrow(() -> new RuntimeException("找不到電影 ID：" + movieId));

	        MovieTheater theater = theaterRepo.findById(theaterId)
	                .orElseThrow(() -> new RuntimeException("找不到影廳 ID：" + theaterId));

	        MovieShowtime showtime = new MovieShowtime();
	        showtime.setMovieList(movie);
	        showtime.setMovietheater(theater);
	        showtime.setShowdate(date);
	        showtime.setShowtime(time);

	        showtimeRepo.save(showtime);
	    
	    }
	    
	    public boolean deleteShowtimeById(Integer id) {
	        if (showtimeRepo.existsById(id)) {
	        	showtimeRepo.deleteById(id);
	            return true;
	        }
	        return false;
	    }
}
