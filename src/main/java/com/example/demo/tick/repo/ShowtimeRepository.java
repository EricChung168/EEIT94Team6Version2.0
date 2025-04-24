package com.example.demo.tick.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.tick.bean.ShowtimeBean;

public interface ShowtimeRepository extends JpaRepository<ShowtimeBean, Integer> {

	List<ShowtimeBean> findShowtimeByShowdate(String startdate);
	
	@Query("SELECT s FROM ShowtimeBean s WHERE s.showdate = :startdate AND s.showtime = (SELECT MIN(sm.showtime) FROM ShowtimeBean sm WHERE sm.movieid = s.movieid AND sm.showdate = :startdate)")
	List<ShowtimeBean> findOneShowtimePerMovieByDate(@Param("startdate") String startdate);
	
	@Query("SELECT s FROM ShowtimeBean s WHERE s.movieid= :id")
	List<ShowtimeBean> findShowtimeBymovieid(@Param("id")Integer id);
}
