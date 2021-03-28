package com.prox.reservas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.prox.reservas.dto.PersonDTO;
import com.prox.reservas.repository.PessoaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonService {
	private final PessoaRepository repository;
	
	public Optional<PersonDTO> buscaPessoaPorId(Long id) {
		log.info("Buscando por ID: {}", id);
		return repository.findById(id);
	}
	
	public List<PersonDTO> buscarTodas(String nome) {
		log.info("Buscando todas Pessoas cadastradas");
//		if (!ObjectUtils.isEmpty(nome))
//			return repository.findByNomeContainingIgnoreCase(nome);
		return repository.findAll();
	}
	
	public void salvarPessoa(PersonDTO personDTO) {
		repository.save(personDTO);
	}

	public void delete(PersonDTO person) {
		repository.delete(person);
	}
}
