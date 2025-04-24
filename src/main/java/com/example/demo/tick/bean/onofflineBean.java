package com.example.demo.tick.bean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity@Table(name = "onofflinevu")
@Setter
@Getter
@NoArgsConstructor
public class onofflineBean {
	@Id @Column(name = "onofflinesid")
	private int onofflineid;
	@Column(name = "hallid")
	private int hallid; 
	@Column(name = "moviename")
	private String moviename;
	@Column(name = "movieid")
	private int movieid;
	
}
