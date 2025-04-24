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
@Entity @Table(name = "viewdata")
@Setter
@Getter
@NoArgsConstructor
public class ShowtimeBean {
	@Id @Column(name = "showtimeid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int showtimeid;
	@Column(name = "showdate")
	private String showdate;
	@Column(name = "showtime")
	private String showtime; 
	@Column(name="movieName")
	private String movieName;
	@Column(name="movieid")
	private String movieid;
	
}
