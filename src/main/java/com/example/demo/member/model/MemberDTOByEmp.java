package com.example.demo.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTOByEmp {
	private Long memberId;
	private String name;
	private String gender;
	private String email;
	private String phoneNumber;
	private String createTime; // 用 String 直接傳 yyyy-MM-dd 格式
}
