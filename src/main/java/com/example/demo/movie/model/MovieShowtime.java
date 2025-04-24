package com.example.demo.movie.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "showtime")
public class MovieShowtime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer showtimeid;

	@ManyToOne
	@JoinColumn(name = "movieid")
	private MovieList movieList;

	@ManyToOne
	@JoinColumn(name = "theaterid")
	private MovieTheater movietheater;

	@Column(name = "showdate")
	private LocalDate showdate;

	@Column(name = "showtime")
	private LocalTime showtime;

	public MovieShowtime() {
		
	}

	public Integer getShowtimeid() {
		return showtimeid;
	}

	public void setShowtimeid(Integer showtimeid) {
		this.showtimeid = showtimeid;
	}

	public MovieList getMovieList() {
		return movieList;
	}

	public void setMovieList(MovieList movieList) {
		this.movieList = movieList;
	}

	public MovieTheater getMovietheater() {
		return movietheater;
	}

	public void setMovietheater(MovieTheater movietheater) {
		this.movietheater = movietheater;
	}

	public LocalDate getShowdate() {
		return showdate;
	}

	public void setShowdate(LocalDate showdate) {
		this.showdate = showdate;
	}

	public LocalTime getShowtime() {
		return showtime;
	}

	public void setShowtime(LocalTime showtime) {
		this.showtime = showtime;
	}

}
