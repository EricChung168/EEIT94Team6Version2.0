package com.example.demo.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        e.printStackTrace(); // 👉 開發期間用來追錯
        model.addAttribute("message", e.getMessage());
        return "foodmenuusers/error";
    }
}
