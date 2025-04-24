package com.example.demo.movieNews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.movieNews.model.MovieNews;
import com.example.demo.movieNews.model.MovieNewsRepository;

import java.util.Date;
import java.util.List;

@Service
public class MovieNewsService {

    @Autowired
    private MovieNewsRepository repository;

    public List<MovieNews> findAll() {
        return repository.findAll();
    }

    public Page<MovieNews> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public MovieNews create(MovieNews news) {
        Date now = new Date();
        if (news.getPublishDate() == null) {
            news.setPublishDate(new Date()); // 自動填入當下時間
        }
        news.setCreatedAt(now);
        news.setUpdatedAt(now);
        news.setPublishDate(new Date());
        return repository.save(news);
    }

    public MovieNews update(MovieNews news) {
        MovieNews existing = repository.findById(news.getId()).orElseThrow();

        existing.setTitle(news.getTitle());
        existing.setSummary(news.getSummary());
        existing.setContent(news.getContent());
        existing.setImageUrl(news.getImageUrl());
        existing.setType(news.getType());
        
        String status = news.getStatus();
        if (status == null || status.isBlank()) {
            status = "active";
        }
        existing.setStatus(status);
        
        if (news.getPublishDate() != null) {
            existing.setPublishDate(news.getPublishDate());
        }
        existing.setUpdatedAt(new Date());
        existing.setIsAd(news.getIsAd() != null ? news.getIsAd() : false);
        

        return repository.save(existing);
    }

    public boolean delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<MovieNews> search(String keyword) {
        return repository.findByTitleContainingIgnoreCaseOrSummaryContainingIgnoreCase(keyword, keyword);
    }


    public MovieNews findById(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public Page<MovieNews> searchByPage(String keyword, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCaseOrSummaryContainingIgnoreCase(keyword, keyword, pageable);
    }
} 