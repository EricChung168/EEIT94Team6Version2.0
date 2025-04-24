package com.example.demo.movieNews.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageViewController {

    @GetMapping("/public1")
    public String showPublicPage() {
        return "public"; // 對應 templates/public.html
    }

    @GetMapping("/news-detail1")
    public String showNewsDetailPage() {
        return "news-detail"; // 對應 templates/news-detail.html
    }

    @GetMapping("/admin1")
    public String showAdminPage() {
        return "admin"; // 對應 templates/admin.html
    }
}
