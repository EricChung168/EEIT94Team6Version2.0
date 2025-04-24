package com.example.demo.usersmenu.controller;

import com.example.demo.usersmenu.model.OrderHeader;
import com.example.demo.usersmenu.model.OrderItem;
import com.example.demo.usersmenu.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class MemberOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/foodmenu/memberFoodOrders")
    public String showFoodOrders(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("memberId");


        Map<OrderHeader, List<OrderItem>> orderMap = orderService.groupItemsByOrder(userId);
        model.addAttribute("orderMap", orderMap);
        System.out.print(orderMap);
        return "foodmenu/memberFoodOrders";
    }

}
