package com.prox.reservas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//https://www.baeldung.com/spring-boot-crud-thymeleaf

@Controller
public class HomeController {
	
	@GetMapping("/index")
	public String showUserList(Model model) {
	    return "index";
	}
	
}
