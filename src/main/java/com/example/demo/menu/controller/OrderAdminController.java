package com.example.demo.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.menu.service.OrderQueryService;
import com.example.demo.usersmenu.model.OrderHeader;
import com.example.demo.usersmenu.model.OrderItem;
import com.example.demo.usersmenu.service.OrderItemService;

@Controller
@RequestMapping("/admin/orders")
public class OrderAdminController {

    @Autowired
    private OrderQueryService orderQueryService;
    @Autowired
    private OrderItemService orderItemService;


    @GetMapping("/search")
    public String showSearchPage() {
        return "foodmenuadmin/order_search";
    }

    @PostMapping("/result")
    public String searchByPhone(@RequestParam String phone, Model model) {
        List<OrderHeader> orders = orderQueryService.findOrdersByPhone(phone);
        model.addAttribute("orders", orders);
        model.addAttribute("phone", phone);
        return "foodmenuadmin/order_result";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Integer orderId, Model model) {
        List<OrderItem> items = orderItemService.findByOrderId(orderId);
        model.addAttribute("items", items);
        model.addAttribute("orderId", orderId);
        return "foodmenuadmin/order_detail";
    }

}
