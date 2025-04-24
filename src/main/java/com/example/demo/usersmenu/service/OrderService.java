package com.example.demo.usersmenu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.usersmenu.model.OrderHeader;
import com.example.demo.usersmenu.model.OrderHeaderRepository;
import com.example.demo.usersmenu.model.OrderItem;
import com.example.demo.usersmenu.model.OrderItemRepository;
import com.example.demo.usersmenu.model.SessionCartItem;
import com.example.demo.usersmenu.model.UsersMenu;
import com.example.demo.usersmenu.model.UsersMenuRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	@Autowired
	private OrderHeaderRepository orderHeaderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private UsersMenuRepository usersMenuRepository;

	@Transactional
	public void processOrder(Long long1, Map<Integer, SessionCartItem> cart, String phone) {
		try {
			System.out.println("🧾 [開始建立訂單] userId=" + long1 + ", 商品數量=" + cart.size());

			OrderHeader header = new OrderHeader();
			header.setUserId(long1);

			header.setOrderTime(LocalDateTime.now());

			List<Integer> menuIds = cart.values().stream()
					.map(SessionCartItem::getMenuId)
					.distinct()
					.toList();

			List<UsersMenu> menus = usersMenuRepository.findAllById(menuIds);
			Map<Integer, UsersMenu> menuMap = menus.stream()
					.collect(Collectors.toMap(UsersMenu::getMenuId, m -> m));

			List<OrderItem> orderItems = new ArrayList<>();
			int total = 0;

			for (SessionCartItem sessionItem : cart.values()) {
				UsersMenu menu = menuMap.get(sessionItem.getMenuId());
				if (menu == null) {
					System.out.println("⚠️ 找不到商品 menuId=" + sessionItem.getMenuId() + "，略過");
					continue;
				}

				OrderItem item = new OrderItem();
				item.setOrder(header);
				item.setMenuId(menu.getMenuId());
				item.setQuantity(sessionItem.getQuantity());
				item.setUnitPrice(menu.getUnitPrice().intValue());

				int subtotal = item.getUnitPrice() * item.getQuantity();
				item.setSubtotal(subtotal);

				orderItems.add(item);
				total += subtotal;
			}
			header.setPhone(phone);
			header.setTotalAmount(total);
			header.setItems(orderItems);

			orderHeaderRepository.save(header);

			System.out.println("✅ [訂單儲存成功] 金額總計=" + total);
		} catch (Exception e) {
			System.out.println("❌ [訂單儲存失敗]：" + e.getMessage());
			e.printStackTrace();
		}
	}

	public List<OrderItem> findFoodOrdersByUserId(Long userId) {
		List<OrderItem> list = orderItemRepository.findByUserId(userId);

		// ✅ 加上這行！印出查到幾筆資料
		System.out.println("📦 查到筆數：" + list.size());

		// ✅ 加碼每筆都印出來
		for (OrderItem item : list) {
			System.out.println("🧾 訂單：" + item.getOrder().getOrderId() +
					", 菜名：" + item.getMenu().getMenuName() +
					", 數量：" + item.getQuantity());
		}

		return list;
	}
	

	public Map<OrderHeader, List<OrderItem>> groupItemsByOrder(Long userId) {
	    LocalDateTime cutoff = LocalDate.now().minusDays(2).atStartOfDay();

	    List<OrderItem> items = orderItemRepository.findByUserId(userId).stream()
	        .filter(item -> item.getOrder().getOrderTime().isAfter(cutoff))  
	        .collect(Collectors.toList()); 

	    return items.stream()
	        .collect(Collectors.groupingBy(OrderItem::getOrder, LinkedHashMap::new, Collectors.toList()));
	}


}
