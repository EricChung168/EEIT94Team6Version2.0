package com.example.demo.usersmenu.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersMenuRepository extends JpaRepository<UsersMenu, Integer> {
	List<UsersMenu> findByStatus(String status);

	Page<UsersMenu> findByStatus(String status, Pageable pageable);
}
