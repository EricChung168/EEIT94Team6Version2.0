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

@Entity@Table(name = "Booktype")
@Getter
@Setter
@NoArgsConstructor
public class BookTypeBean {
	@Id @Column(name = "tickettypeid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tickettypeid;
	@Column(name = "tickettype")
	private String tickettype;
	@Column(name = "moneytype")
	private int moneytype;
}
