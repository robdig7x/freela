package com.prox.reservas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
	
	public Optional<Person> buscaPessoaPorNome(String nome) {
		log.info("Buscando pelo nome {}", nome);
		return Optional.of(repository.findByNome(nome));
	}
	
	public Optional<Person> buscaPessoaPorId(Long id) {
		log.info("Buscando por ID: {}", id);
		return repository.findById(id);
	}
	
	public List<Person> buscarTodas() {
		log.info("Buscando todas Pessoas cadastradas");
		return repository.findAll();
	}
	
	public Person salvarPessoa(PersonDTO personDTO) {
		return repository.save(personDTO.converter());
	}

	public void delete(Person person) {
		repository.delete(person);
	}
}
