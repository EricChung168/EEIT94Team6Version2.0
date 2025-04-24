package com.example.demo.listener;

import jakarta.servlet.http.HttpSession;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {

    private static final Set<HttpSession> sessions = ConcurrentHashMap.newKeySet();

    public static void addSession(HttpSession session) {
        sessions.add(session);
        System.out.println("增加:"+session);
    }

    public static void removeSession(HttpSession session) {
        sessions.remove(session);
    }

    public static void clearAllSessions() {
        for (HttpSession session : sessions) {
            try {
                session.invalidate(); // 清除 session
                System.out.println("清除:"+session);
            } catch (IllegalStateException e) {
                // 已經失效了
            }
        }
        sessions.clear();
    }
}
