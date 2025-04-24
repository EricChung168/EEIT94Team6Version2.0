package com.example.demo.member.controller;
import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memberManagerApi")
public class MemberApiController {
	
	@Autowired
    private MemberService memberService;
	

	
	@PostMapping("/register")
	public ResponseEntity<?> register(@ModelAttribute Member member) {
	    try {
	        memberService.registerMemberByEmp(member);
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", "success");
	        response.put("name", member.getName());
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", "error");
	        response.put("message", e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}
	
	@GetMapping("/find/{id}")
	public ResponseEntity<Member> findByIdByEmp(@PathVariable Long id) {
	    return ResponseEntity.ok(memberService.findById(id));
	}
	
	@GetMapping("/list")
	public List<Member> getAllMembers() {
	    return memberService.findAll(); // 或直接 memberRepository.findAll()
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody Member member) {
	    try {
	        memberService.updateMemberByEmp(member);
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", "success");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", "error");
	        response.put("message", e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Member>> searchMembers(
	        @RequestParam("type") String type,
	        @RequestParam("keyword") String keyword) {

	    List<Member> result;

	    switch (type) {
	        case "name" -> result = memberService.searchByName(keyword);
	        case "email" -> result = memberService.searchByEmail(keyword);
	        case "phone" -> result = memberService.searchByPhone(keyword);
	        default -> result = List.of(); // 回傳空列表避免錯誤
	    }

	    return ResponseEntity.ok(result);
	}
	
	@PostMapping("/sendVerification/{id}")
	public ResponseEntity<?> sendVerification(@PathVariable Long id) {
	    try {
	        memberService.sendVerificationEmailById(id);
	        return ResponseEntity.ok().build();
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("寄信失敗：" + e.getMessage());
	    }
	}


}
