package com.example.demo.usersmenu.model;

import java.sql.Timestamp; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
	private Integer cartItemId;
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "menu_id")
	private Integer menuId;
	
	private Integer quantity;
	@Column(name = "added_time")
	private Timestamp addedTime;
}
