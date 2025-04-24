package com.example.demo.movie.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movielist")
public class MovieList {
	
	@Id @Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "released")
	private LocalDate released;
	
	@Column(name = "retired")
	private LocalDate retired;
	
	@Column(name = "runtime")
	private Integer runtime;
	
	@Column(name = "state")
	private String state;

	 
	public MovieList() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getReleased() {
		return released;
	}

	public void setReleased(LocalDate released) {
		this.released = released;
	}

	public LocalDate getRetired() {
		return retired;
	}

	public void setRetired(LocalDate retired) {
		this.retired = retired;
	}

	public Integer getRuntime() {
		return runtime;
	}

	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
}
