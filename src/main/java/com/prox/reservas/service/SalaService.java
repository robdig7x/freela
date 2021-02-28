package com.prox.reservas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.prox.reservas.dto.SalaDTO;
import com.prox.reservas.entities.Sala;
import com.prox.reservas.repository.SalaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SalaService {
	
	private final SalaRepository repository;
	
	public Optional<Sala> buscaSalaPorId(Long id) {
		log.info("Buscando por ID: {}", id);
		return repository.findById(id);
	}
	
	public List<Sala> buscarTodas() {
		log.info("Buscando todas Salas cadastradas");
		return repository.findAll();
	}
	
	public Sala salvarSala(SalaDTO salaDTO) {
		return repository.save(salaDTO.converter());
	}

	public void delete(Sala sala) {
		repository.delete(sala);
	}
}
