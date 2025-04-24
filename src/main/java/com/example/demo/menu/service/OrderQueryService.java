package com.example.demo.menu.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.menu.model.UserRepository;
import com.example.demo.menu.model.Users;
import com.example.demo.usersmenu.model.OrderHeader;
import com.example.demo.usersmenu.model.OrderHeaderRepository;
import com.example.demo.usersmenu.model.OrderItem;
import com.example.demo.usersmenu.model.OrderItemRepository;

@Service
public class OrderQueryService {

    @Autowired
    private OrderHeaderRepository orderHeaderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderHeader> findOrdersByPhone(String phone) {
        return orderHeaderRepository.findByPhone(phone);
    }

    public List<OrderItem> findItemsByOrderId(Integer orderId) {
        return orderItemRepository.findByOrderOrderId(orderId);
    }
}

