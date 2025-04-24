package com.example.demo.movie.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.movie.model.MovieShowtime;

public interface MovieShowtimeRepository extends JpaRepository<MovieShowtime, Integer> {
	
	 @Query(value = """
		        SELECT 
		          ml.name AS movieName,
		          md.image AS posterUrl,
		          CONVERT(varchar, ml.released, 23) AS releaseDate,
		          CONCAT(ml.runtime, ' 分鐘') AS duration,
		          md.rating AS level,
		          st.showtime AS showTime
		        FROM showtime st
		        JOIN movielist ml ON st.movieid = ml.id
		        LEFT JOIN moviedetail md ON md.id = ml.id
		        WHERE (:movieId IS NULL OR st.movieid = :movieId)
		          AND (:date IS NULL OR st.showdate = :date)
		        ORDER BY ml.name, st.showtime
		    """, nativeQuery = true)
		    List<Map<String, Object>> findSessionInfoByMovieIdAndDate(
		        @Param("movieId") Integer movieId,
		        @Param("date") String date
		    );
		

}
