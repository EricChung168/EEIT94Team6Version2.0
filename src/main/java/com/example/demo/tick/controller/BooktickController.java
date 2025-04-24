package com.example.demo.tick.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.demo.DemoApplication;
import com.example.demo.tick.bean.BookticketBean;
import com.example.demo.tick.bean.BookticketvuBean;
import com.example.demo.tick.bean.OrderBean;
import com.example.demo.tick.bean.ShowtimeBean;
import com.example.demo.tick.service.BookvuService;
import com.example.demo.tick.service.HallService;
import com.example.demo.tick.service.TickorderService;
import com.example.demo.tick.service.TimeService;

import groovyjarjarantlr4.runtime.misc.IntArray;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BooktickController {


	@Autowired
	private BookvuService bookvuService;
	@Autowired 
	private TickorderService orderService;
	@Autowired
	private HallService hallService;
	@Autowired
	private TimeService timeService;


	
	@GetMapping("/ticktable")
	public String tickall(Model model) {
		List<BookticketvuBean> vu = bookvuService.tickfindAll();
		List<Map.Entry<String, Long>> top3Movies = bookvuService.getTop3PopularMovies();
        model.addAttribute("top3", top3Movies);
		
		model.addAttribute("tickall", vu);
		return "tick/table";
	}

	@GetMapping("/booktick/update")
	public String tickupdatebyid(@RequestParam Integer tickid, Model model) {
		Optional<BookticketvuBean> op = bookvuService.findtickbyid(tickid);

		model.addAttribute("tickinfo", op.get());
		return "tick/updatetick";
	}

	// 處理修改訂票的 POST 請求 (對應表單的 action="Updatea")
	@PostMapping("/booktick/Updatea")
	public String updateTick(@ModelAttribute BookticketBean updatedTick) {
		BookticketvuBean vuBean = bookvuService.findmoneybyid(updatedTick.getTickid());
		
		OrderBean oBean = new OrderBean();
		oBean.setUserid(updatedTick.getMemberId());
		oBean.setOrderid(updatedTick.getOrderid());
		Optional<OrderBean>optional= orderService.findbyorder(updatedTick.getOrderid());
		Integer a = optional.get().getSumpay()-vuBean.getOnemoney()+updatedTick.getOnemoney();
		oBean.setSumpay(a);
		oBean.setOrderdate(optional.get().getOrderdate());
		ShowtimeBean showtimeBean = timeService.findtimedate(updatedTick.getShowtimeid()).get();
		oBean.setShowdate(showtimeBean.getShowdate());
		oBean.setShowtime(showtimeBean.getShowtime());
		oBean.setPayout(updatedTick.getPayout());
		oBean.setMoviename(hallService.findnamebyhallid(updatedTick.getHallid()).getMoviename());
//		oBean.setSeat(updatedTick.getSeatid());
		String seatString = optional.get().getSeat();
		List<String> seats = new ArrayList<>(Arrays.asList(seatString.split(",")));
		for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i).equals(vuBean.getSeatid())) {
                seats.set(i, updatedTick.getSeatid());
                break;
            }
        }

        String updatedSeatString = String.join(",", seats);
        oBean.setSeat(updatedSeatString);
		oBean.setHallid(updatedTick.getHallid());
		orderService.update(oBean);
		bookvuService.updatetick(updatedTick);
		return "redirect:/ticktable"; // 更新成功後重定向到訂票列表頁面
	}
	@PostMapping("/booktick/inser")
	public String inserTick(@ModelAttribute BookticketBean updatedTick) {
		OrderBean oBean = new OrderBean();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formattera = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		Long order = Long.parseLong(formatter.format(now));
		oBean.setUserid(updatedTick.getMemberId());
		oBean.setOrderdate(formattera.format(now));
		oBean.setOrderid(order);
		oBean.setSumpay(updatedTick.getOnemoney());
		oBean.setHallid(updatedTick.getHallid());
		oBean.setSeat(updatedTick.getSeatid());
		oBean.setMoviename(hallService.findnamebyhallid(updatedTick.getHallid()).getMoviename());
		ShowtimeBean showtimeBean = timeService.findtimedate(updatedTick.getShowtimeid()).get();
		oBean.setShowdate(showtimeBean.getShowdate());
		oBean.setShowtime(showtimeBean.getShowtime());
		oBean.setPayout(updatedTick.getPayout());
		updatedTick.setOrderid(order);
		orderService.inser(oBean);
		bookvuService.updatetick(updatedTick);
		return "redirect:/ticktable"; // 更新成功後重定向到訂票列表頁面
	}

	@GetMapping("/booktick/inser")
	public String inser() {
		return "tick/inser";
	}

	@GetMapping("/booktick/seat")
	@ResponseBody
	public List<BookticketBean> getMethodName(@RequestParam Integer hallid, @RequestParam Integer showtimeid) {
		return bookvuService.findseatbyhallid(hallid, showtimeid);
	}

	@GetMapping("/booktick/del")
	public String dele(@RequestParam Integer id) {
		Optional<BookticketvuBean>optional =  bookvuService.findtickbyid(id);
		
		Optional<OrderBean> op2 = orderService.findbyorder(optional.get().getOrderid());
		int a= op2.get().getSumpay()-optional.get().getOnemoney();
		op2.get().setSumpay(a);
		orderService.update(op2.get());
		bookvuService.deltick(id);
		return "redirect:/ticktable";
	}

	///////////// other
	@GetMapping("/booktick/other")
	public String findother(@RequestParam Integer select,
			@RequestParam(required = false) Integer tickid, @RequestParam(required = false) Long orderid,
			@RequestParam(required = false) Long memberId, @RequestParam(required = false) String startdate,
			@RequestParam(required = false) Integer hallid, @RequestParam(required = false) String findname,
			@RequestParam(required = false) String payout,Model model

	) {
		List<BookticketvuBean> vu = null;
		if (select == 1) {
			vu=bookvuService.findother(select, tickid,null);
			
		} else if (select == 2) {
			vu= bookvuService.findothers(select, null,orderid);
		} else if (select == 3) {
			vu= bookvuService.findother(select, null,memberId);
		} else if (select == 4) {
			vu= bookvuService.findothers(select, startdate,null);
		} else if (select == 5) { 
			vu= bookvuService.findother(select, hallid,null);
		}else if (select == 6) {
			System.out.println(findname);
			vu= bookvuService.findothers(select, findname,null);
		}else if (select == 7) {
			vu= bookvuService.findothers(select, payout,null);
		}
		
		model.addAttribute("tickall", vu);
		return "tick/table";
		
	}

}
