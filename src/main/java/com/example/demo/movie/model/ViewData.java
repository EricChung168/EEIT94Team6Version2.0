package com.example.demo.movie.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity

@Table(name = "viewdata")
public class ViewData {

	@Id
	private Integer showtimeId;

	private Integer movieId;
	private Integer theaterId;
	private String movieName;
	private LocalDate showDate;
	private LocalTime showTime;
	@Transient
	private String theaterName;

	public Integer getShowtimeId() {
		return showtimeId;
	}
	
	public String getTheaterName() {
	    return theaterName;
	}
	
	public void setTheaterName(String theaterName) {
	    this.theaterName = theaterName;
	}


	public void setShowtimeId(Integer showtimeId) {
		this.showtimeId = showtimeId;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	public Integer getTheaterId() {
		return theaterId;
	}

	public void setTheaterId(Integer theaterId) {
		this.theaterId = theaterId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public LocalDate getShowDate() {
		return showDate;
	}

	public void setShowDate(LocalDate showDate) {
		this.showDate = showDate;
	}

	public LocalTime getShowTime() {
		return showTime;
	}

	public void setShowTime(LocalTime showTime) {
		this.showTime = showTime;
	}
}