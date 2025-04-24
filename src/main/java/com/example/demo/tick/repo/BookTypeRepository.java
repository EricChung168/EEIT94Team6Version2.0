package com.example.demo.tick.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.tick.bean.BookTypeBean;

public interface BookTypeRepository extends JpaRepository<BookTypeBean, Integer>{
	 @Query("SELECT b FROM BookTypeBean b WHERE b.tickettypeid = :id")
	BookTypeBean findMoneytypebyTickettypeid(Integer id);
}
