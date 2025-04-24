package com.example.demo.member.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/memberManager")
public class MemberManagerController {

	//會員後臺首頁
	@GetMapping("/index")
	public String MemberManager() {
		return "member/MemberManager";
	}
	
	@GetMapping("/memberList")
	public String memberList() {
		return "member/memberList";
		
	}
	
}
