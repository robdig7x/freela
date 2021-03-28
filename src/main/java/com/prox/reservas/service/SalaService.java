package com.prox.reservas.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.prox.reservas.dto.SalaDTO;
import com.prox.reservas.repository.SalaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SalaService {
	
	private final SalaRepository repository; 
	
	public Optional<SalaDTO> buscaSalaPorId(Long id) {
		log.info("Buscando por ID: {}", id);
		return repository.findById(id);
	}

	public List<SalaDTO> buscarTodas(String keyword) {
		log.info("Buscando todas Salas cadastradas");
		
		List<SalaDTO> salas = repository.findAll();
		
		// TODO: Melhorar a filtragem para eliminar a necessidade de buscar todos antes do filtro.
		
		if (!ObjectUtils.isEmpty(keyword))
			salas = salas.stream().filter(sala -> sala.getNome().contains(keyword)).collect(Collectors.toList());
			
		return salas;
		
	}
	
	public void delete(SalaDTO sala) {
		repository.deleteById(sala.getId());
	}
	
	public void save(SalaDTO sala) {
		repository.save(sala);
	}

	

}
