package com.example.demo.menu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.menu.model.Menu;
import com.example.demo.menu.model.MenuRepository;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository repo;

    public List<Menu> findAll() {
        return repo.findAll();
    }

    public Page<Menu> findAll(Pageable pageable) {
        return repo.findAll(pageable); 
    }

    public void save(Menu menu) {
        repo.save(menu);
    }

    public Menu findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
}
