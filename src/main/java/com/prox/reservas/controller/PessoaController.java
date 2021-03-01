package com.prox.reservas.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prox.reservas.dto.PersonDTO;
import com.prox.reservas.dto.SalaDTO;
import com.prox.reservas.entities.Person;
import com.prox.reservas.entities.Sala;
import com.prox.reservas.service.PersonService;
import com.prox.reservas.service.SalaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//https://www.baeldung.com/spring-boot-crud-thymeleaf

@RequestMapping("pessoa")
@Controller
@RequiredArgsConstructor
@Slf4j
public class PessoaController {
	
	private static final String REDIRECT_PERSON = "redirect:/pessoa";
	
	private final PersonService service;
	private final SalaService salaService;
	
	@GetMapping
	public String showSignupForm(@Param("keyword") String keyword, PersonDTO person, Model model) {
		List<Person> buscarTodas = service.buscarTodas(keyword);
		model.addAttribute("persons", buscarTodas.isEmpty() ? null : buscarTodas);
		model.addAttribute("keyword", keyword);	
		return "add-person";
	}
	
	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		Person person = service.buscaPessoaPorId(id)	
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		List<Sala> todasSalas = salaService.buscarTodas("");
		model.addAttribute("person", new PersonDTO(person));
		model.addAttribute("todasSalas", SalaDTO.converter(todasSalas));		
		return "update-person";
	}
	
	@PostMapping("/add")
	@Transactional
	public String addPessoa(@Valid PersonDTO personDTO) {
		service.salvarPessoa(personDTO);
		return REDIRECT_PERSON;
	}
	
	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") Long id, @Valid PersonDTO personDTO, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "update-person";
	    }
	    
	    personDTO.setId(id);
	    service.salvarPessoa(personDTO);
	    return REDIRECT_PERSON;
	}
	    
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		Person person = service.buscaPessoaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + id));
		service.delete(person);
	    log.info("Pessoa deletada: {}", new PersonDTO(person));
	    return REDIRECT_PERSON;
	}
}
