package com.prox.reservas.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prox.reservas.dto.PersonDTO;
import com.prox.reservas.entities.Person;
import com.prox.reservas.service.PersonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
	
	private final PersonService service;
	
	@GetMapping
	public ResponseEntity<PersonDTO> buscaPorNomeDaPessoa(String nome) {
		Optional<Person> person = service.buscaPessoaPorNome(nome);
		return obtainResponsePerson(person);
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<PersonDTO> buscaPorId(@PathVariable Long id) {
		Optional<Person> person = service.buscaPessoaPorId(id);
		return obtainResponsePerson(person);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<PersonDTO> cadastrarPessoa(@Valid @RequestBody PersonDTO personDTO, UriComponentsBuilder uriBuilder) {
		Person person = service.salvarPessoa(personDTO);
		URI uri = uriBuilder.path("/person/{id}").buildAndExpand(person.getId()).toUri();
		return ResponseEntity.created(uri).body(new PersonDTO(person));
	}
		
	private ResponseEntity<PersonDTO> obtainResponsePerson(Optional<Person> person) {
		if (person.isPresent())
			return ResponseEntity.ok(new PersonDTO(person.get()));		
		
		return ResponseEntity.notFound().build();
	}
}
