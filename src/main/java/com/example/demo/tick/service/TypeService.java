package com.example.demo.tick.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.tick.bean.BookTypeBean;
import com.example.demo.tick.repo.BookTypeRepository;

@Service
public class TypeService {

	@Autowired
	private BookTypeRepository bookTypeRepository;
	
	public List<BookTypeBean> findtype() {
		return bookTypeRepository.findAll();
		
	}
	
	public BookTypeBean findmony(Integer id) {
		return bookTypeRepository.findMoneytypebyTickettypeid(id);
	}
	
}
