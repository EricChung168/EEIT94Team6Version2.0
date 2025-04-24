package com.example.demo.tick.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.demo.tick.bean.BookTypeBean;
import com.example.demo.tick.bean.HallsBean;
import com.example.demo.tick.bean.MovieInfoBean;
import com.example.demo.tick.bean.ShowtimeBean;
import com.example.demo.tick.bean.onofflineBean;
import com.example.demo.tick.service.HallService;
import com.example.demo.tick.service.TimeService;
import com.example.demo.tick.service.TypeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/booktick")
public class TimeController {
	@Autowired
	private TimeService timeService;
	@Autowired
	private HallService hallService;
	@Autowired
	private TypeService typeService;
	
	
	///改成找movieid
	@PostMapping("time/{startdate}")
	@ResponseBody
	public List<ShowtimeBean> findtimebydate(@PathVariable String startdate) {
		 
		List<ShowtimeBean> list = timeService.findtimebydate(startdate);
		
		return list;
	}
	@PostMapping("timename/{startdate}")
	@ResponseBody
	public List<ShowtimeBean> findnamebydate(@PathVariable String startdate) {
		
		List<ShowtimeBean> list = timeService.findnamebydate(startdate);
		
		return list;
	}
	@PostMapping("timena/{nameid}")
	@ResponseBody
	public List<ShowtimeBean> findtimebynameid(@PathVariable Integer nameid) {
		
		List<ShowtimeBean> list = timeService.findtimebynameid(nameid);
		
		return list;
	}
	@PostMapping("timese/{showtimeid}")
	@ResponseBody
	public ShowtimeBean getMethodName(@PathVariable Integer showtimeid) {
		
		Optional<ShowtimeBean> op =timeService.findtimedate(showtimeid);
		return op.get();
	}
	
	
	
//	@GetMapping("hall")
//	@ResponseBody
//	public List<HallsBean> findallhall() {
//		
//		return hallService.findall();
//	}
	@GetMapping("hall")
	@ResponseBody
	public List<onofflineBean> findallname() {
		
		return hallService.findallonline();
	}
	@GetMapping("name")
	@ResponseBody
	public onofflineBean findname(@RequestParam Integer nameid) {
		
		
		return hallService.findhallbyname(nameid);
	}
	@GetMapping("namega")
	@ResponseBody
	public Optional<onofflineBean> findnamega(@RequestParam Integer nameid) {
		
		
		
		return hallService.findhallbyname2(nameid);
	}
	@GetMapping("type")
	@ResponseBody
	public List<BookTypeBean> findalltype() {
		return typeService.findtype();
	}
	@GetMapping("Allname")
	@ResponseBody
	public List<MovieInfoBean> nameall() {
		
		return hallService.findAll();
	}

	
	
}
