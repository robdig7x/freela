package com.prox.reservas.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prox.reservas.dto.SalaDTO;
import com.prox.reservas.service.SalaService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/sala-fire")
@RestController
@RequiredArgsConstructor
public class SalaFirestoreController {
	
	private final SalaService salaService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<SalaDTO> addSala(@RequestBody SalaDTO salaDTO) {
		salaService.save(salaDTO);
		return ResponseEntity
	            .created(URI
	                     .create(String.format("/sala/%s", salaDTO.getId())))
	            .body(salaDTO);
	}
	
	@GetMapping
	public List<SalaDTO> findAll() {
		return salaService.buscarTodas("");
	}
	
}
