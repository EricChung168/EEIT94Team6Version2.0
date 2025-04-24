package com.example.demo.employee.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	public Employee insertEmployee(Employee employee) {
		String originalPwd =  employee.getPassword();
		String hashedPwd = pwdEncoder.encode(originalPwd);
		employee.setPassword(hashedPwd); 

		Employee insertBean = employeeRepository.save(employee);
		return insertBean;
	}
	
	
	
	public Employee login(String email,String originalPwd) {
		Employee employee = employeeRepository.findByEmail(email);
		if(employee!=null) {
			boolean result = pwdEncoder.matches(originalPwd, employee.getPassword());
		
			if(result) {
				return employee;
			}
		}
		return null;

	}
	public Employee findById(UUID id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		if (optional.isPresent()) {
			Employee employee = optional.get();
			return employee;
		}
		return null;
		
	}

	public boolean empExistCheck(Employee employee) {
		String email = employee.getEmail();
		String phoneNumber = employee.getPhoneNumber();
		
//		System.out.println(email + " "+phoneNumber);
		//檢查信箱&手機號碼是否已註冊
		List<Employee> existEmployees = employeeRepository.findByEmailOrPhoneNumber(email,phoneNumber);
		if(!existEmployees.isEmpty()) {
			System.out.println("已被註冊");
			//已存在回傳false 不給註冊
			return false;
		}else {
			//未存在回傳true 表示允許註冊
			System.out.println("可以註冊");
			return true;
		}
	}
}
