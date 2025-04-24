package com.example.demo.usersmenu.model;

public class CartDisplayItem {

	private Integer menuId;
	private String menuName;
	private Integer quantity;
	private Integer unitPrice;
	private Integer subtotal;
	private String imageUrl;
	public Integer getMenuId() {
		return menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Integer subtotal) {
		this.subtotal = subtotal;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getImageUrl() {
	    return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
	    this.imageUrl = imageUrl;
	}
	
}
