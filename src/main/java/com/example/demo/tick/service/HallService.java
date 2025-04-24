package com.example.demo.tick.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.tick.bean.HallsBean;
import com.example.demo.tick.bean.MovieInfoBean;
import com.example.demo.tick.bean.onofflineBean;
import com.example.demo.tick.repo.HallsRepository;
import com.example.demo.tick.repo.MoveinfoRepository;
import com.example.demo.tick.repo.onoffRepository;

@Service
public class HallService {

	@Autowired
	private HallsRepository hallsRepository;
	@Autowired
	private onoffRepository onoffRepository;
	@Autowired
	private MoveinfoRepository moveinfoRepository;
	public List<HallsBean> findall() {
		return hallsRepository.findAll();
	}
	
	public onofflineBean findnamebyhallid(Integer id) {
		return onoffRepository.findMovienameByHallid(id);
	}
	public onofflineBean findhallbyname(Integer id) {
		return onoffRepository.findHallidByMovieid(id);
	}
	public Optional<onofflineBean> findhallbyname2(Integer id) {
		return onoffRepository.findDistinctHallidsByMovieid(id);
	}
	public List<onofflineBean> findallonline() {
		return onoffRepository.findAll();
	}
	public List<MovieInfoBean> findAll() {
		return moveinfoRepository.findAll();
		
	}
	
}
