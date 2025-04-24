package com.example.demo.testController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AppController {
	
	@GetMapping("/template")
	public String template() {
		return "fragments/empTemplate";
	}
	
	
	
	
}
