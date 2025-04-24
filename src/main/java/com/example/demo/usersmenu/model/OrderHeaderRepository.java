package com.example.demo.usersmenu.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Integer> {
    List<OrderHeader> findByPhone(String phone);
}
