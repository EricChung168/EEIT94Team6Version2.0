package com.example.demo.movie.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.movie.model.MovieShowtime;
import com.example.demo.movie.model.MovieTheater;
import com.example.demo.movie.repo.MovieShowtimeRepository;
import com.example.demo.movie.repo.MovieTheaterRepository;
import com.example.demo.movie.service.MovieShowtimeService;

@RestController
@RequestMapping("/showtime")
public class ShowtimeController {

    @Autowired
    private MovieShowtimeService showtimeService;

    @Autowired
    private MovieTheaterRepository theaterRepo;
    
    @Autowired
    private MovieShowtimeRepository movieShowtimeRepository;

    // 新增場次
    @PostMapping("/add")
    public ResponseEntity<String> addShowtime(@RequestBody MovieShowtime request) {
        try {
            showtimeService.addShowtime(
                    request.getMovieList().getId(),
                    request.getMovietheater().getTheaterid(),
                    request.getShowdate(),
                    request.getShowtime()
            );
            return ResponseEntity.ok("新增成功！");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("新增失敗：" + e.getMessage());
        }
    }

    @GetMapping("/theaters")
    public List<MovieTheater> getTheaters() {
        return theaterRepo.findAll();
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteShowtime(@PathVariable Integer id) {
        boolean deleted = showtimeService.deleteShowtimeById(id);
        if (deleted) {
            return ResponseEntity.ok("刪除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到場次");
        }
    }
    
    @GetMapping("/search")
    public List<Map<String, Object>> searchSessions(
            @RequestParam(required = false) Integer movieId,
            @RequestParam(required = false) String date) {
        return movieShowtimeRepository.findSessionInfoByMovieIdAndDate(movieId, date);
    }
}