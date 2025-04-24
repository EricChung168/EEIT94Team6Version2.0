package com.example.demo.usersmenu.model;

import java.io.Serializable;

public class SessionCartItem implements Serializable {

	private Integer menuId;
	private Integer quantity;
	
	
	public SessionCartItem(Integer menuId, Integer quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}
	
	public SessionCartItem() {
		
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
