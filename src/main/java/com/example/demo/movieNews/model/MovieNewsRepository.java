package com.example.demo.movieNews.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieNewsRepository extends JpaRepository<MovieNews, Integer> {

	List<MovieNews> findByTitleContaining(String keyword);
	
	List<MovieNews> findByTitleContainingIgnoreCaseOrSummaryContainingIgnoreCase(String keyword1, String keyword2);

	List<MovieNews> findByStatus(String status);
	
	List<MovieNews> findByIsAdTrue();
	
	Page<MovieNews> findByTitleContainingIgnoreCaseOrSummaryContainingIgnoreCase(String keyword1, String keyword2, Pageable pageable);


}
