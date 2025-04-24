package com.example.demo.employee.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee,UUID> {
		//Employee登入用
	Employee findByEmail(String email);
	//Employee 註冊時檢查 信箱和密碼是否已註冊
	List<Employee> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
