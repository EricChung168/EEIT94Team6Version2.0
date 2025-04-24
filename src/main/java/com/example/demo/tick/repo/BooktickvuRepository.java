package com.example.demo.tick.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.tick.bean.BookticketvuBean;
import com.example.demo.tick.service.BookvuService;

public interface BooktickvuRepository extends JpaRepository<BookticketvuBean, Integer> {

	@Query("SELECT b FROM BookticketvuBean b WHERE b.tickid = :id")
    List<BookticketvuBean> findbyTickid(@Param("id") Integer id);
	@Query("SELECT b FROM BookticketvuBean b WHERE b.tickid = :id")
	BookticketvuBean findbymoney(@Param("id") Integer id);

    @Query("SELECT b FROM BookticketvuBean b WHERE b.orderid = :id")
    List<BookticketvuBean> findbyOrderid(@Param("id") Long id);

    @Query("SELECT b FROM BookticketvuBean b WHERE b.memberId = :id")
    List<BookticketvuBean> findbyUserid(@Param("id") Long id);

    @Query("SELECT b FROM BookticketvuBean b WHERE b.showtimedate = :date")
    List<BookticketvuBean> findbyShowtimedate(@Param("date") String date);

    @Query("SELECT b FROM BookticketvuBean b WHERE b.hallid = :id")
    List<BookticketvuBean> findbyHallid(@Param("id") Integer id);

    @Query("SELECT b FROM BookticketvuBean b WHERE b.moviename = :name")
    List<BookticketvuBean> findbyMoviename(@Param("name") String name);

    @Query("SELECT b FROM BookticketvuBean b WHERE b.payout = :pay")
    List<BookticketvuBean> findbyPayout(@Param("pay") String pay);

    
}
