package com.example.demo.movieNews.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/chat")
public class ChatMemberController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/members")
    public List<Map<String, Object>> getAllMembersForChat() {
        List<Member> members = memberRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Member m : members) {
            Map<String, Object> map = new HashMap<>();
            map.put("memberId", m.getMemberId());
            map.put("name", m.getName());
            result.add(map);
        }

        return result;
    }
    
    @GetMapping("/session-id")
    public String getSessionMemberId(HttpSession session) {
        Object id = session.getAttribute("memberId");
        return id != null ? id.toString() : "";
    }
    
}

