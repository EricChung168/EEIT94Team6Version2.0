package com.example.demo.tick.usercontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.tick.bean.BookticketvuBean;
import com.example.demo.tick.bean.OrderBean;
import com.example.demo.tick.service.BookvuService;
import com.example.demo.tick.service.TickorderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class OrderdetilController {

	@Autowired
	private BookvuService bookvuService;
	@Autowired
	private TickorderService tickorderService;
	
	@GetMapping("/detail")
	public String gotodetil(HttpServletRequest request,Model model) {
		HttpSession session = request.getSession();
		// 假設你在登入時將會員 ID 存放在 session 中，key 為 "memberId"
		Long memberId = (Long) session.getAttribute("memberId");
		List<OrderBean> order = tickorderService.findbyuserid(memberId);
		
//		List<BookticketvuBean> vBookticketvuBeans = bookvuService.findother(3, null,memberId);
		model.addAttribute("detil", order);
		return "order/orderdetails";
	}
	
	
}
