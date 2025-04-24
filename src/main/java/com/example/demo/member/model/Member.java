package com.example.demo.member.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "MEMBER")
public class Member {
//	個人訊息
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;
	
	@Column(name = "name",length = 15,nullable = false)
	private String name;
	
	@Column(name = "gender",nullable = false)
	private String gender;
	
	
	@Column(name = "nationalId",nullable = false,length = 10)
	private String nationalId;
	
//	認證相關
	@Column(name = "email",nullable = false)
	private String email;
	
	
	@Column(name = "phoneNumber",nullable = false,length = 10,unique = true)
	private String phoneNumber;
	
	@Column(name = "password",nullable = false, length = 255)
	private String password;
	
	@Column(name = "newEmail")
	private String newEmail;
	
	@Column(name = "verification")
	private boolean verification = false;
	
	@Column(name = "verificationCode")
	private String verificationCode = null;
	
	@CreationTimestamp
    @Column(name = "createTime", nullable = false, updatable = false)
    private LocalDateTime createTime;
	
	@Column(name = "dateOfBirth", nullable = false)
    private String dateOfBirth;
	
	
	
	
}
