package com.example.demo.usersmenu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.usersmenu.model.OrderItem;
import com.example.demo.usersmenu.model.OrderItemRepository;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItem> findByOrderId(Integer orderId) {
        return orderItemRepository.findByOrderOrderId(orderId);
    }
}