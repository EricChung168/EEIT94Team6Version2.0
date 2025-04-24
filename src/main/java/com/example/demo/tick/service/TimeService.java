package com.example.demo.tick.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.tick.bean.ShowtimeBean;
import com.example.demo.tick.repo.ShowtimeRepository;

@Service
public class TimeService {
	
	@Autowired
	private ShowtimeRepository showtimeRepository;
	
	public List<ShowtimeBean> findtimebydate(String date) {
		return showtimeRepository.findShowtimeByShowdate(date);
	}
	public List<ShowtimeBean> findtimebynameid(Integer movieid) {
		return showtimeRepository.findShowtimeBymovieid(movieid);
	}
	public List<ShowtimeBean> findnamebydate(String date) {
		return showtimeRepository.findOneShowtimePerMovieByDate(date);
	}
	
	public Optional<ShowtimeBean> findtimedate(Integer id) {
		return showtimeRepository.findById(id);
	}
}
