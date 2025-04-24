package com.example.demo.usersmenu.model;

import java.util.List;

public class CartViewResponse {
	private List<CartDisplayItem> items;
	private Integer total;
	
	public List<CartDisplayItem> getItems(){
		return items;
	}
	
	public void setItems(List<CartDisplayItem> items) {
		this.items = items;
	}
	
	public Integer getTotal() {
		return total;
	}
	
	public void setTotal(Integer total) {
		this.total = total;
	}

}
