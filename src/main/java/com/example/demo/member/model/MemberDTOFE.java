package com.example.demo.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTOFE {
	private Long memberId;
	private String name;
	private String gender;
	private String email;
	private String phoneNumber;
	private String nationalId;
	private String dateOfBirth; // 這邊傳成字串比較好處理
	private String memberType;
}
