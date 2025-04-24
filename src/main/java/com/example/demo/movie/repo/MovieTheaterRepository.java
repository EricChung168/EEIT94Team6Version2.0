package com.example.demo.movie.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.movie.model.MovieTheater;

public interface MovieTheaterRepository extends JpaRepository<MovieTheater,Integer> {

}
