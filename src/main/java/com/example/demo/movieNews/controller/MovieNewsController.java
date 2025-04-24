package com.example.demo.movieNews.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.movieNews.model.MovieNews;
import com.example.demo.movieNews.model.MovieNewsRepository;
import com.example.demo.movieNews.service.MovieNewsService;

@RestController
@RequestMapping("/api/news")
public class MovieNewsController {

	@Autowired
	MovieNewsRepository repository;
    @Autowired
    private MovieNewsService service;

   

    @GetMapping("/search")
    public List<MovieNews> search(@RequestParam String keyword) {
        return repository.findByTitleContainingIgnoreCaseOrSummaryContainingIgnoreCase(keyword, keyword);
    }


    @PostMapping
    public MovieNews addNews(@RequestBody MovieNews news) {
        return service.create(news);
    }

    @PutMapping("/{id}")
    public MovieNews updateNews(@PathVariable int id, @RequestBody MovieNews news) {
        news.setId(id);
        return service.update(news);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable int id) {
        boolean success = service.delete(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public Page<MovieNews> getAllNews(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @GetMapping("/list")
    public List<MovieNews> getAllAsList() {
        return service.findAll();
    }
    
    @GetMapping("/{id}")
    public MovieNews getNewsById(@PathVariable int id) {
        return service.findById(id);
    }
    
    @PostMapping("/upload") 
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads", filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        return Map.of("url", "/uploads/" + filename);
    }

    
    @GetMapping("/active")
    public List<MovieNews> getActiveNews() {
        return repository.findByStatus("active");
    }
    
    @GetMapping("/ads")
    public List<MovieNews> getAds() {
        return repository.findByIsAdTrue();
    }
    
    @GetMapping("/search-page")
    public Page<MovieNews> searchWithPage(@RequestParam String keyword, Pageable pageable) {
        return service.searchByPage(keyword, pageable);
    }

}