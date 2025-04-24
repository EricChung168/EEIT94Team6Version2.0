package com.example.demo.tick.bean;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "Bookorder")
@Setter
@Getter
@NoArgsConstructor
public class OrderBean {

	@Id
	private Long orderid; 
	private Long userid;
	private int sumpay;
	private String orderdate;
	private  String moviename;
	private  int hallid;
	private  String showtime;
	private  String showdate;
	private String seat;
	
	private String payout;
}
