package com.example.demo.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class CustomSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        SessionRegistry.addSession(se.getSession());
        System.out.println("Session 建立，ID: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        SessionRegistry.removeSession(se.getSession());
        System.out.println("Session 銷毀，ID: " + se.getSession().getId());
    }
    
}
