package com.example.demo.tick.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.tick.bean.OrderBean;
import com.example.demo.tick.repo.OrderRepository;

@Service
public class TickorderService {


	@Autowired
	private OrderRepository orderRepository;

	//依userid找全部
	public List<OrderBean> findbyuserid(Long id) {
		return orderRepository.findByUserid(id);
	}
	public Optional<OrderBean> findbyorder(Long order) {
		return orderRepository.findById(order);
	}
	//新增
	public OrderBean inser(OrderBean orderBean) {
		return orderRepository.save(orderBean);
	}
	//修改
	public OrderBean update(OrderBean orderBean) {
		return orderRepository.save(orderBean);
	}
	
	public void uppay(Long orderid) {
		orderRepository.updatePayoutByOrderId(orderid, "Y");
	}
}
