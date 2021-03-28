package com.prox.reservas.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.prox.reservas.dto.SalaDTO;
import com.prox.reservas.service.SalaService;

import lombok.RequiredArgsConstructor;

//https://www.baeldung.com/spring-boot-crud-thymeleaf

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final SalaService service;
	
	@GetMapping("/index")
	public String showUserList(Model model) {
		List<SalaDTO> buscarTodasAsSalas = service.buscarTodas("");
		model.addAttribute("salas", buscarTodasAsSalas.isEmpty() ? null : buscarTodasAsSalas);
	    return "index";
	}
	
}
