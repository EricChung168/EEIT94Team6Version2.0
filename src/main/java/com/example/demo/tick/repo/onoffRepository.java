package com.example.demo.tick.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.tick.bean.ShowtimeBean;
import com.example.demo.tick.bean.onofflineBean;
import java.util.List;
import java.util.Optional;


public interface onoffRepository extends JpaRepository<onofflineBean, Integer> {

	onofflineBean  findMovienameByHallid(Integer hallid);
	onofflineBean  findHallidByMovieid(Integer movieid);
	
	
	@Query("SELECT DISTINCT s.hallid FROM onofflineBean s WHERE s.movieid = :movieid")
	Optional<onofflineBean> findDistinctHallidsByMovieid(@Param("movieid") int movieid);

}
