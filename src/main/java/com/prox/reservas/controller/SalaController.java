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
		List<SalaDTO> listaDeSalas = salaService.buscarTodas(keyword);
		model.addAttribute("salas", listaDeSalas.isEmpty() ? null : listaDeSalas);
		model.addAttribute("keyword", keyword);	
		return "add-sala";
	}
	
	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		SalaDTO sala = salaService.buscaSalaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sala Id:" + id));
		
		model.addAttribute("sala", sala);
		return "update-sala";
	}
	
	@GetMapping("/manage/{id}")
	public String showManageForm(@PathVariable("id") Long id, Model model) {
		SalaDTO sala = salaService.buscaSalaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sala Id:" + id));
		
		List<PersonDTO> listaDePessoas = personService.buscarTodas("");
		model.addAttribute("persons", listaDePessoas.isEmpty() ? null : listaDePessoas);

		model.addAttribute("sala", sala);
		return "manage-sala";
	}
	
	@PostMapping("/manage/{acao}/{idSala}/pessoa/{idPessoa}")
	public String showManageForm(@PathVariable("idSala") Long idSala, @PathVariable("idPessoa") Long idPessoa, @PathVariable("acao") String acao, Model model) {
		SalaDTO sala = salaService.buscaSalaPorId(idSala)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sala Id:" + idSala));
		PersonDTO person = personService.buscaPessoaPorId(idPessoa)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + idPessoa));
		
		if (ObjectUtils.isEmpty(acao))
			throw new IllegalArgumentException("Acao parameter não informado");
		
		if ("R".equalsIgnoreCase(acao) && person.getSalaId().equals(idSala.toString())) {
			sala.getPessoas().remove(person);
			person.setSalaId(null);
		} else 
		if ("I".equalsIgnoreCase(acao)) {
			sala.getPessoas().add(person);
			person.setSalaId(sala.getId());
		} else {
			throw new IllegalArgumentException("Acao parameter não informado");
		}
		salaService.save(sala);
		personService.salvarPessoa(person);
		return "redirect:/sala/manage/".concat(idSala.toString());
	}

	@PostMapping("/add")
	@Transactional
	public String addSala(@Valid SalaDTO salaDTO) {
		salaService.save(salaDTO);
		return REDIRECT_SIGNUP;
	}
	
	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") Long id, @Valid SalaDTO salaDTO, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "update-sala";
	    }
	    salaService.save(salaDTO);
	    return REDIRECT_SIGNUP;
	}
	    
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		SalaDTO sala = salaService.buscaSalaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sala Id:" + id));
		salaService.delete(sala);
	    log.info("Pessoa deletada: {}", sala);
	    return REDIRECT_SIGNUP;
	}
}
