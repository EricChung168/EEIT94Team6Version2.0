package com.example.demo.movieNews.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.movieNews.model.ChatMessage;
import com.example.demo.movieNews.model.ChatMessageRepository;

@Controller
public class ChatController {

	  @Autowired
	    private ChatMessageRepository chatMessageRepository;
	  @Autowired
	  private SimpMessagingTemplate messagingTemplate;

	    @MessageMapping("/chat")
	    public void send(ChatMessage message, SimpMessageHeaderAccessor accessor) {
	        Long sessionMemberId = (Long) accessor.getSessionAttributes().get("memberId");
	        String sessionMemberName = (String) accessor.getSessionAttributes().get("memberName");

	        Long memberId = (message.getMemberId() != null) ? message.getMemberId() : sessionMemberId;
	        String memberName = (message.getSender() != null) ? message.getSender() : sessionMemberName;

	        if (memberId == null || memberName == null) {
	            throw new IllegalArgumentException("memberId 為必填");
	        }

	        message.setMemberId(memberId);
	        message.setSender(memberName);
	        message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
	        chatMessageRepository.save(message);

	        // ✅ 改為發送給目標會員專屬頻道
	        messagingTemplate.convertAndSend("/topic/private/" + memberId, message);
	    }
	}