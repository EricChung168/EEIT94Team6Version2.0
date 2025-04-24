package com.example.demo.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoviePageController {

	@GetMapping("/MovieList")
	public String ListPage() {
		return "/Movie";
	}
	
	@GetMapping("/showtime")
	public String ShowtimePage() {
		return "/Showtime";
	}
	@GetMapping("/ShowtimeSelect")
	public String ShowtimeSelectPage() {
		return "/ShowtimeSelect";
	}
	@GetMapping("/MovieInfo")
	public String MovieInfoPage() {
		return "/MovieInfo";
	}
}
