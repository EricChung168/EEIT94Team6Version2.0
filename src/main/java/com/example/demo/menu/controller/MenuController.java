package com.example.demo.menu.controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.menu.model.Menu;
import com.example.demo.menu.service.MenuService;
import com.example.demo.usersmenu.model.OrderItemRepository;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService service;
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    // ✅ 首頁導向第 1 頁（取代原本的 list）
    @GetMapping("/list")
    public String redirectToFirstPage() {
        return "redirect:/menu/page/1";
    }

    // ✅ 分頁查詢
    @GetMapping("/page/{page}")
    public String getMenuPage(@PathVariable("page") int page, Model model) {
        int pageSize = 7;
        if(page < 1) {
        	return "redirect:/menu/page/1";
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("menuId").descending());
        Page<Menu> pageData = service.findAll(pageable);
        
        if(page > pageData.getTotalPages() && pageData.getTotalPages() > 0) {
        	 return "redirect:/menu/page/" + pageData.getTotalPages();
        }
        model.addAttribute("menuItems", pageData.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageData.getTotalPages());

        return "foodmenu/menu_list";
    }

    // 刪除
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return "redirect:/menu/page/1";
    }

    // 編輯畫面
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Menu menu = service.findById(id);
        if(menu !=null) {
        	model.addAttribute("menu",menu);
        	return "foodmenu/menu_edit";
        }else {
        	return "redirect:/menu/page/1";
        }
    }
    // 更新
    @PostMapping("/update")
    public String updateMenu(@ModelAttribute Menu menu,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String filename = imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                menu.setImageUrl("/uploads/" + filename);  // ✅ 設定新圖
            } else {
                // 如果沒選圖片，保留舊圖：從 DB 找出舊圖網址
                Menu oldMenu = service.findById(menu.getMenuId());
                if (oldMenu != null) {
                    menu.setImageUrl(oldMenu.getImageUrl());
                }
            }

            service.save(menu);
            return "redirect:/menu/page/1";

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }


    // 新增畫面
    @GetMapping("/new")
    public String createMenuForm(Model model) {
        model.addAttribute("menu", new Menu());
        return "foodmenu/menu_new";
    }

    // 新增動作
    @PostMapping("/save")
    public String saveMenu(@ModelAttribute Menu menu,
                           @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String filename = imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);

                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ 圖片已儲存於：" + filePath.toAbsolutePath());

                menu.setImageUrl("/uploads/" + filename);
            } else {
                System.out.println("⚠️ 沒有選擇圖片！");
            }
            service.save(menu);
            return "redirect:/menu/list";

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("🔥 MenuController 被掃到了！");
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object error = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("status", status);
        model.addAttribute("error", error);
        model.addAttribute("message", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));

        return "error";
    }
    
    @GetMapping("/statistics")
    public String showMenuStatistics(Model model) {
        List<Object[]> top5 = orderItemRepository.findTop5MenuSales();
        List<Object[]> bottom5 = orderItemRepository.findBottom5MenuSales();

        model.addAttribute("top5", top5);
        model.addAttribute("bottom5", bottom5);
        return "foodmenu/menu_statistics";
    }



}

