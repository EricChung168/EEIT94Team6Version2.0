package com.example.demo.menu.model;





import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;





@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menu")
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Integer menuId; 


    @Column(name = "menu_name")
    private String menuName;

    private int stock;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "unit_price")
    private Integer unitPrice;

    private String description;

    private String status;

    private String category;
}
