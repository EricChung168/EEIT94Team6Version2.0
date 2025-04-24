package com.example.demo.usersmenu.util;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.usersmenu.model.SessionCartItem;

public class TempTradeDataStore {
    public static final Map<String, Integer> userMap = new HashMap<>();
    public static final Map<String, Map<Integer, SessionCartItem>> cartMap = new HashMap<>();
    public static Map<String, TempOrderData> tradeDataMap = new HashMap<>();
    
}
