package com.example.demo.member.model;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
	//Member登入用
	Member findByEmail(String email);
	List<Member> findByEmailContaining(String email);
	List<Member> findByNationalIdContaining(String nationalId);
	List<Member> findByPhoneNumberContaining(String phoneNumber);
	//Member 註冊時檢查 信箱和密碼是否已註冊
	List<Member> findByEmailOrPhoneNumber(String email, String phoneNumber);
	Member findByVerificationCode(String code);
	
	List<Member> findByNameContaining(String name);
	
	
	List<Member> findByNameContainingIgnoreCase(String name);
	List<Member> findByEmailContainingIgnoreCase(String email);

}
