package com.example.demo.tick.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.tick.bean.HallsBean;

public interface HallsRepository extends JpaRepository<HallsBean, Integer> {

}
