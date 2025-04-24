package com.example.demo.usersmenu.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	@Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.menu WHERE oi.order = :order")
	List<OrderItem> findItemsWithMenuByOrder(@Param("order") OrderHeader order);

	List<OrderItem> findByOrderOrderId(Integer orderId);

	@Query(value = "SELECT TOP 5 m.menu_name, SUM(oi.quantity) AS total " +
			"FROM order_item oi JOIN usersmenu m ON oi.menu_id = m.menu_id " +
			"GROUP BY m.menu_name ORDER BY total DESC", nativeQuery = true)
	List<Object[]> findTop5MenuSales();

	@Query(value = "SELECT TOP 5 m.menu_name, SUM(oi.quantity) AS total " +
			"FROM order_item oi JOIN usersmenu m ON oi.menu_id = m.menu_id " +
			"GROUP BY m.menu_name ORDER BY total ASC", nativeQuery = true)
	List<Object[]> findBottom5MenuSales();
	

	@Query("SELECT i FROM OrderItem i JOIN FETCH i.order JOIN FETCH i.menu WHERE i.order.userId = :userId")
	List<OrderItem> findByUserId(@Param("userId") Long userId);


}
