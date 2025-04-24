package com.example.demo.usersmenu.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.usersmenu.model.UsersMenu;
import com.example.demo.usersmenu.model.UsersMenuRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usersmenu")
public class UsersMenuController {
    @Autowired
    private UsersMenuRepository usersMenuRepository;

    @GetMapping("/page/{pageNumber}")
    public String getMenusByPage(@PathVariable("pageNumber") int pageNumber, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
        // 假設你在登入時將會員 ID 存放在 session 中，key 為 "memberId"
        Long memberId = (Long) session.getAttribute("memberId");
        System.out.println(memberId);
        if(memberId==null) {
        	return "member/login";
        }
    	
        int pageSize = 6;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<UsersMenu> page = usersMenuRepository.findByStatus("上架", pageable);
        model.addAttribute("menus", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());

        return "foodmenuusers/usermenu_list";
    }
    
    

}
