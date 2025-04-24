package com.example.demo.movie.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.movie.model.ViewData;
import com.example.demo.movie.service.ViewDataService;

@RestController
@RequestMapping("/viewdata")
public class ViewDataController {

    @Autowired
    private ViewDataService viewDataService;

    @GetMapping("/search")
    public List<ViewData> searchShowtimes(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(required = false) Integer movieId,
        @RequestParam(required = false) Integer theaterId) {

        return viewDataService.searchShowtimes(date, movieId, theaterId);
    }
}