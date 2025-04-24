package com.example.demo.movie.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.movie.model.ViewData;

public interface ViewDataRepository extends JpaRepository<ViewData, Integer> {
	
	@Query("SELECT v FROM ViewData v WHERE "
		       + "(:movieId IS NULL OR v.movieId = :movieId) AND "
		       + "(:date IS NULL OR v.showDate = :date) AND "
		       + "(:theaterId IS NULL OR v.theaterId = :theaterId) "
		       + "ORDER BY v.showDate, v.showTime")
		List<ViewData> search(
		    @Param("date") LocalDate date,
		    @Param("movieId") Integer movieId,
		    @Param("theaterId") Integer theaterId
		);
}
