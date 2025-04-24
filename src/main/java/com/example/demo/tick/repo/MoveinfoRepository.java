package com.example.demo.tick.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.tick.bean.MovieInfoBean;

public interface MoveinfoRepository extends JpaRepository<MovieInfoBean, Integer> {

}
