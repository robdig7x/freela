package com.prox.reservas.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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

@RequestMapping("/sala")
@Controller
@RequiredArgsConstructor
@Slf4j
public class SalaController {
	
	private static final String REDIRECT_SIGNUP = "redirect:/sala";
	
	private final SalaService salaService;
	private final PersonService personService;
	
	@GetMapping
	public String showSignupForm(@Param("keyword") String keyword, SalaDTO sala, Model model) {
		List<SalaDTO> listaDeSalas = SalaDTO.converter(salaService.buscarTodas(keyword));
		model.addAttribute("salas", listaDeSalas.isEmpty() ? null : listaDeSalas);
		model.addAttribute("keyword", keyword);	
		return "add-sala";
	}
	
	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		Sala sala = salaService.buscaSalaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sala Id:" + id));
		
		model.addAttribute("sala", new SalaDTO(sala));
		return "update-sala";
	}
	
	@GetMapping("/manage/{id}")
	public String showManageForm(@PathVariable("id") Long id, Model model) {
		Sala sala = salaService.buscaSalaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sala Id:" + id));
		
		List<PersonDTO> listaDePessoas = PersonDTO.converter(personService.buscarTodas(""));
		model.addAttribute("persons", listaDePessoas.isEmpty() ? null : listaDePessoas);

		model.addAttribute("sala", new SalaDTO(sala));
		return "manage-sala";
	}
	
	@PostMapping("/manage/{acao}/{idSala}/pessoa/{idPessoa}")
	public String showManageForm(@PathVariable("idSala") Long idSala, @PathVariable("idPessoa") Long idPessoa, @PathVariable("acao") String acao, Model model) {
		Sala sala = salaService.buscaSalaPorId(idSala)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sala Id:" + idSala));
		Person person = personService.buscaPessoaPorId(idPessoa)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + idPessoa));
		
		if (ObjectUtils.isEmpty(acao))
			throw new IllegalArgumentException("Acao parameter não informado");
		
		if ("R".equalsIgnoreCase(acao) && person.getSala().getId().equals(idSala)) {
			sala.getPessoas().remove(person);
			person.setSala(null);
		} else if ("I".equalsIgnoreCase(acao)) {
			person.setSala(sala);
			sala.getPessoas().add(person);
		} else {
			throw new IllegalArgumentException("Acao parameter não informado");
		}
		salaService.salvarSala(new SalaDTO(sala));
		personService.salvarPessoa(new PersonDTO(person));
		return "redirect:/sala/manage/".concat(idSala.toString());
	}

	@PostMapping("/add")
	@Transactional
	public String addSala(@Valid SalaDTO salaDTO) {
		salaService.salvarSala(salaDTO);
		return REDIRECT_SIGNUP;
	}
	
	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") Long id, @Valid SalaDTO salaDTO, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "update-sala";
	    }
	    salaService.salvarSala(salaDTO);
	    return REDIRECT_SIGNUP;
	}
	    
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		Sala sala = salaService.buscaSalaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sala Id:" + id));
		salaService.delete(sala);
	    log.info("Pessoa deletada: {}", new SalaDTO(sala));
	    return REDIRECT_SIGNUP;
	}
}
