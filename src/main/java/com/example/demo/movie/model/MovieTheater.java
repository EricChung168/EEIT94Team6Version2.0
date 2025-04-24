package com.example.demo.movie.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "theater")
public class MovieTheater {

	@Id
	@Column(name = "theaterid")
	private Integer theaterid;

	@Column(name = "name")
	private String name;

	public MovieTheater() {

	}

	public Integer getTheaterid() {
		return theaterid;
	}

	public void setTheaterid(Integer theaterid) {
		this.theaterid = theaterid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
