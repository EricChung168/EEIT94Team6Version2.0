package com.example.demo.usersmenu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usersmenu")
public class UsersMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Integer menuId;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "unit_price")
    private Float unitPrice;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;

    private String category;

   
}
