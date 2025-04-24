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

@Entity @Table(name = "movietickvu")
@NoArgsConstructor
@Setter
@Getter
public class BookticketvuBean {
	@Id@Column(name = "tickid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tickid;
	@Column(name = "orderid")
	private Long orderid;
	@Column(name = "memberId")
	private int memberId;
	//private String startdate;
	@Column(name = "showid")
	private int showtimeid;
	@Column(name = "seatid")
	private String seatid;
	@Column(name = "hallid")
	private int hallid;
	@Column(name = "onemony")
	private int onemoney;
	

	@Column(name = "payout")
	private String payout;
	@Column(name = "showtimedate")
	private String showtimedate;
	@Column(name = "showtime")
	private String showtime;
	@Column(name = "ticktype")
	private String booktype;
	@Column(name = "ticktypeid")
	private int tickettypeid;
	@Column(name = "moviename")
	private String moviename;
}
