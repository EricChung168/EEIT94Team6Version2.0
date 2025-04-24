package com.example.demo.movie.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.example.demo.movie.model.MovieDetail;
import com.example.demo.movie.model.MovieList;
import com.example.demo.movie.repo.MovieDetailRepository;
import com.example.demo.movie.service.MovieDetailService;
import com.example.demo.movie.service.MovieListService;


@RestController
@RequestMapping("/movies")  // 路徑設置為 /movies 
public class MovieController {

    @Autowired
    private MovieListService movieListService;
    
    @Autowired
    private MovieDetailRepository movieDetailRepository;
    
    @Autowired
    private MovieDetailService movieDetailService;

    // 查詢所有電影
    @GetMapping
    
    public List<MovieList> findAll() {
        return movieListService.findAll();
    }

    // 查詢單部電影
    @GetMapping("/{id}")
    public ResponseEntity<MovieList> findById(@PathVariable Integer id) {
        MovieList movie = movieListService.findById(id).get();
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }

    // 修改單部電影
    @PutMapping("/{id}")
    public ResponseEntity<MovieList> updateMovie(@PathVariable Integer id, @RequestBody MovieList movieDetails) {
        MovieList updatedMovie = movieListService.updateMovie(id, movieDetails);
        if (updatedMovie != null) {
            return ResponseEntity.ok(updatedMovie);
        }
        return ResponseEntity.notFound().build();
    }
    
    // 修改單部電影詳細訊息
    @PutMapping("/{id}/detail")
    public ResponseEntity<MovieDetail> updateMovie(@PathVariable Integer id, @RequestBody MovieDetail movieDetails) {
        MovieDetail updatedMovie = movieDetailService.updateMovieDetail(id, movieDetails);
        if (updatedMovie != null) {
            return ResponseEntity.ok(updatedMovie);
        }
        return ResponseEntity.notFound().build();
    }

    // 刪除單部電影
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        if (movieListService.deleteById(id)) {
            return ResponseEntity.noContent().build();  // 刪除成功
        }
        return ResponseEntity.notFound().build();  // 找不到電影
    }
    
    @PostMapping
    public ResponseEntity<MovieList> createMovie(@RequestBody MovieList movie) {
        MovieList savedMovie = movieListService.save(movie);
        movieDetailService.addMovieDetail(movie.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie); // 回傳 201 Created
    }
    
    @GetMapping("/{id}/details")
    public ResponseEntity<MovieDetail> getMovieDetails(@PathVariable Integer id) {
        Optional<MovieDetail> movieDetail = movieDetailRepository.findById(id);
        if (movieDetail.isPresent()) {
            return ResponseEntity.ok(movieDetail.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file,@RequestParam("imageName") String imageName) {
        try {
            // 設定儲存路徑
            String uploadDir = "src/main/resources/static/movie/images/";
            
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                return ResponseEntity.badRequest().body("檔案名無效。");
            }

            String fileExtension = getFileExtension(originalFileName);
            System.out.println(fileExtension);
            Path targetLocation = Paths.get(uploadDir + imageName +fileExtension);

            // 儲存檔案
            Files.copy(file.getInputStream(), targetLocation);

            return ResponseEntity.ok("圖片上傳成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("圖片上傳失敗");
        }
    }
    //抓取副檔名
    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            return fileName.substring(index);  // 擷取副檔名
        }
        return "";  // 如果找不到副檔名，返回空字串
    }
    
    //上架中的電影
    @GetMapping("/available")
    public List<MovieList> getAvailableMovies() {
        return movieListService.getAvailableMovies();
    }
 
}
