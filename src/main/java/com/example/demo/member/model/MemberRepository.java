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
	Member findByEmailAndOriginEmailCode(String email, String originEmailCode);
	
	
	
	// 查找註冊時的驗證信用code
    Member findByOriginEmailCode(String originEmailCode);

    // 查找忘記密碼用的重設密碼code
    Member findByResetPasswordCode(String resetPasswordCode);

    // 查找信箱變更時的新信箱驗證code
    Member findByNewEmailCode(String newEmailCode);
	Member findByEmailAndResetPasswordCode(String email, String code);
}
