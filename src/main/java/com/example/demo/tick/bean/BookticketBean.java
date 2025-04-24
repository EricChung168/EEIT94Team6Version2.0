package com.example.demo.tick.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "Booktickets")
@Setter
@Getter
@NoArgsConstructor
public class BookticketBean {
	@Id@Column(name = "tickid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tickid;
	@Column(name = "orderid")
	private Long orderid;
	@Column(name = "memberId")
	private Long memberId;
	@Column(name = "showtimeid")
	private int showtimeid;
	@Column(name = "seatid")
	private String seatid;
	@Column(name = "hallid")
	private int hallid;
	@Column(name = "onemoney")
	private int onemoney;
	@Column(name = "movieid")
	private int movieid;
	@Column(name = "tickettypeid")
	private int tickettypeid;
	@Column(name = "payout")
	private String payout;
	public BookticketBean(Long orderid, Long memberId, int showtimeid, String seatid, int hallid, int onemoney,
			int movieid, int tickettypeid, String payout) {
		super();
		this.orderid = orderid;
		this.memberId = memberId;
		this.showtimeid = showtimeid;
		this.seatid = seatid;
		this.hallid = hallid;
		this.onemoney = onemoney;
		this.movieid = movieid;
		this.tickettypeid = tickettypeid;
		this.payout = payout;
	}
	public BookticketBean(int showtimeid, String seatid, int hallid, int onemoney, int movieid, int tickettypeid,
			String payout) {
		super();
		this.showtimeid = showtimeid;
		this.seatid = seatid;
		this.hallid = hallid;
		this.onemoney = onemoney;
		this.movieid = movieid;
		this.tickettypeid = tickettypeid;
		this.payout = payout;
	}
}
