package com.example.demo.movie.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.movie.model.MovieList;

public interface MovieListRepository extends JpaRepository<MovieList, Integer> {
	
	List<MovieList> findByState(String state); 

}
