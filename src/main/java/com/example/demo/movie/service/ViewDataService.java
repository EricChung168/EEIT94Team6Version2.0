package com.example.demo.movie.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.movie.model.ViewData;
import com.example.demo.movie.repo.MovieTheaterRepository;
import com.example.demo.movie.repo.ViewDataRepository;

@Service
public class ViewDataService {

	@Autowired
	private ViewDataRepository viewDataRepository;

	@Autowired
	private MovieTheaterRepository movieTheaterRepository;

	public List<ViewData> searchShowtimes(LocalDate date, Integer movieId, Integer theaterId) {
	    List<ViewData> viewDataList = viewDataRepository.search(date, movieId, theaterId);

	    for (ViewData v : viewDataList) {
	        movieTheaterRepository.findById(v.getTheaterId()).ifPresent(t -> v.setTheaterName(t.getName()));
	    }

	    return viewDataList;
	}
}