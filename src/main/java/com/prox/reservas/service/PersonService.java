package com.prox.reservas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.prox.reservas.dto.PersonDTO;
import com.prox.reservas.entities.Person;
import com.prox.reservas.repository.PersonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonService {
	private final PersonRepository repository;
	
	public Optional<Person> buscaPessoaPorId(Long id) {
		log.info("Buscando por ID: {}", id);
		return repository.findById(id);
	}
	
	public List<Person> buscarTodas(String nome) {
		log.info("Buscando todas Pessoas cadastradas");
		if (!ObjectUtils.isEmpty(nome))
			return repository.findByNomeContainingIgnoreCase(nome);
		return repository.findAll();
	}
	
	public Person salvarPessoa(PersonDTO personDTO) {
		return repository.save(personDTO.converter());
	}

	public void delete(Person person) {
		repository.delete(person);
	}
}
