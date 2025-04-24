package com.example.demo.tick.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.tick.bean.OrderBean;

public interface OrderRepository extends JpaRepository<OrderBean, Long> {

	 List<OrderBean> findByUserid(Long userid);
	 
	 @Query("UPDATE OrderBean o SET o.payout = :payout WHERE o.orderid = :orderid")
	 @Modifying
	 void updatePayoutByOrderId(@Param("orderid") Long orderid, @Param("payout") String payout);
}
