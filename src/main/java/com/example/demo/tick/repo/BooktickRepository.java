package com.example.demo.tick.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.tick.bean.BookticketBean;

public interface BooktickRepository extends JpaRepository<BookticketBean, Integer> {

	@Query("SELECT b FROM BookticketBean b WHERE b.hallid = :hallid AND b.showtimeid = :showtimeid")
	List<BookticketBean> findSeatidbyHallidShowtimeid(@Param("hallid") Integer hallid,
			@Param("showtimeid") Integer showtimeid);

	@Query("UPDATE BookticketBean o SET o.payout = :payout WHERE o.orderid = :orderid")
	@Modifying
	void updatePayoutByOrderIda(@Param("orderid") Long orderid, @Param("payout") String payout);

	@Query(value = "SELECT TOP 3 ml.name AS moviename, COUNT(bt.movieid) AS ticketCount " +
            "FROM Booktickets bt JOIN Movielist ml ON bt.movieid = ml.id " +
            "GROUP BY ml.name ORDER BY ticketCount DESC", nativeQuery = true)
List<Map<String, Object>> findTop3MovieNamesWithTicketCountNative();

default List<Map.Entry<String, Long>> getTop3MovieNamesByTicketsNative() {
 List<Map<String, Object>> results = findTop3MovieNamesWithTicketCountNative();
 return results.stream()
         .map(map -> Map.entry((String) map.get("moviename"), ((Number) map.get("ticketCount")).longValue()))
         .toList();
}

}
