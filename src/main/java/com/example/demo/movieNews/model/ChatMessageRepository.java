package com.example.demo.movieNews.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByMemberIdOrderByTimestampAsc(Long memberId);
}
