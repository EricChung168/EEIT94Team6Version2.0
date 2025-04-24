package com.example.demo.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class EcpayCheckMacUtil {

    public static String generateCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
        try {
            // 1. 排序參數（不分大小寫）
            SortedMap<String, String> sorted = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            sorted.putAll(params);

            // 2. 組成字串：HashKey + 參數 + HashIV
            StringBuilder sb = new StringBuilder("HashKey=").append(hashKey);
            for (Map.Entry<String, String> entry : sorted.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            sb.append("&HashIV=").append(hashIV);

            // 3. URL Encode（符合綠界格式）
            String urlEncoded = URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8.name())
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%2A", "*")
                    .replaceAll("\\%2D", "-")
                    .replaceAll("\\%2E", ".")
                    .replaceAll("\\%5F", "_")
                    .toLowerCase(); // ⚠️ 小寫！

            // 4. SHA256 加密並轉大寫
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(urlEncoded.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02X", b));
            }

            return hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

