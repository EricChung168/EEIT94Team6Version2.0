package com.example.demo.usersmenu.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.usersmenu.model.SessionCartItem;
import com.example.demo.usersmenu.model.UsersMenu;
import com.example.demo.usersmenu.model.UsersMenuRepository;
import com.example.demo.usersmenu.service.OrderService;
import com.example.demo.usersmenu.util.TempOrderData;
import com.example.demo.usersmenu.util.TempTradeDataStore;
import com.example.demo.util.EcpayCheckMacUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class EcpayController {

    private final OrderService orderService;
    private final UsersMenuRepository usersMenuRepository;

    public EcpayController(OrderService orderService, UsersMenuRepository usersMenuRepository) {
        this.orderService = orderService;
        this.usersMenuRepository = usersMenuRepository;
    }

    @PostMapping("/ecpay")
    
    public String goEcpay(@RequestParam("phone") String phone,
                          HttpSession session,
                          Model model) {
    	
        Long userId =   (Long) session.getAttribute("memberId");
        System.out.println("🔐 當前登入 userId = " + userId);
        Map<Integer, SessionCartItem> cart = (Map<Integer, SessionCartItem>) session.getAttribute("cart");

        if (userId == null || cart == null || cart.isEmpty()) {
            return "redirect:/cart.html";
        }

        int total = 0;
        for (SessionCartItem item : cart.values()) {
            UsersMenu menu = usersMenuRepository.findById(item.getMenuId()).orElse(null);
            if (menu == null) continue;
            total += menu.getUnitPrice().intValue() * item.getQuantity();
        }

        String merchantID = "2000132";
        String tradeNo = "ORD" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15);
        String tradeDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        String returnURL = "https://088b-36-228-235-238.ngrok-free.app/checkout/confirm";
        String clientBackURL = "http://localhost:8080";

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MerchantID", merchantID);
        paramMap.put("MerchantTradeNo", tradeNo);
        paramMap.put("MerchantTradeDate", tradeDate);
        paramMap.put("PaymentType", "aio");
        paramMap.put("TotalAmount", String.valueOf(total));
        paramMap.put("TradeDesc", "購物車測試");
        paramMap.put("ItemName", "訂單商品");
        paramMap.put("ReturnURL", returnURL);
        paramMap.put("ClientBackURL", clientBackURL);
        paramMap.put("NeedExtraPaidInfo", "N");
        paramMap.put("ChoosePayment", "ALL");

        String hashKey = "5294y06JbISpM5x9";
        String hashIV = "v77hoKGq4kWxNNIS";
        String checkMacValue = EcpayCheckMacUtil.generateCheckMacValue(paramMap, hashKey, hashIV);

        // ✅ 使用封裝好的 TempOrderData 儲存所有資料
        TempOrderData data = new TempOrderData();
        data.setUserId(userId);
        data.setCart(cart);
        data.setPhone(phone);
        TempTradeDataStore.tradeDataMap.put(tradeNo, data);
        
        System.out.println("📦 儲存 TempTradeData：tradeNo = " + tradeNo + ", userId = " + userId);

        model.addAttribute("merchantID", merchantID);
        model.addAttribute("tradeNo", tradeNo);
        model.addAttribute("tradeDate", tradeDate);
        model.addAttribute("total", total);
        model.addAttribute("returnURL", returnURL);
        model.addAttribute("clientBackURL", clientBackURL);
        model.addAttribute("checkMacValue", checkMacValue);

        return "foodmenuusers/ecpayform";
    }

    @PostMapping("/confirm")
    public String confirmPayment(@RequestParam("MerchantTradeNo") String tradeNo) {
        System.out.println("✅ 進入 confirmPayment() tradeNo=" + tradeNo);

        TempOrderData data = TempTradeDataStore.tradeDataMap.get(tradeNo);
        if (data != null && data.getUserId() != null && data.getCart() != null && !data.getCart().isEmpty()) {
            orderService.processOrder(data.getUserId(), data.getCart(), data.getPhone());
            TempTradeDataStore.tradeDataMap.remove(tradeNo);
            System.out.println("✅ 成功儲存訂單，phone=" + data.getPhone());
        } else {
            System.out.println("⚠️ 找不到對應資料，無法儲存訂單");
        }

        return "redirect:/success.html";
    }
}
