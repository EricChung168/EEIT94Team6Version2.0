package com.example.demo.movie.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.movie.model.MovieDetail;

public interface MovieDetailRepository extends JpaRepository<MovieDetail, Integer> {

}
