package com.example.demo.tick.bean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity@Table(name = "theater")
@Getter
@Setter
@NoArgsConstructor
public class HallsBean {
	@Id @Column(name = "theaterid")
	private int theaterid;
	
}
