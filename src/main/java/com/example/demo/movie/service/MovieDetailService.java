package com.example.demo.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.movie.model.MovieDetail;
import com.example.demo.movie.repo.MovieDetailRepository;

@Service
public class MovieDetailService {

	@Autowired
	private MovieDetailRepository movieDetailRepository;
	
	// 更新電影詳細資料
	public MovieDetail updateMovieDetail(Integer id, MovieDetail movieDetail) {
		if (movieDetailRepository.existsById(id)) {
			movieDetail.setId(id);
			return movieDetailRepository.save(movieDetail);
		}
		return null;
	}
	
	public MovieDetail addMovieDetail(Integer movieId) {
        MovieDetail movieDetail = new MovieDetail();
		movieDetail.setId(movieId);
        // 儲存 MovieDetail 到資料庫
		return movieDetailRepository.save(movieDetail);
    }
}
