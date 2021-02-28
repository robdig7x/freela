package com.prox.reservas.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;

import com.prox.reservas.entities.Person;
import com.prox.reservas.entities.Sala;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDTO {
	
	private Long id;
	
	@NotEmpty
	@NotNull(message = "Nome não pode ser nulo.")
	@Size(min = 2, message = "O Nome deve possuir no minimo 2 Caracteres")
	private String nome;
	
	private Sala sala;
	
	@NotBlank(message = "Name is mandatory")
	private String sobrenome;
	
	public PersonDTO(Person person) {
		if (person != null)
			BeanUtils.copyProperties(person, this);
	}

	public Person converter() {
		return new Person(id, nome, sobrenome, sala);
	}
	
	public static List<PersonDTO> converter(List<Person> persons) {
		return persons.stream().map(PersonDTO::new).collect(Collectors.toList());
	}
	
	public boolean estaNaSala() {
		return this.sala != null;
	}
}
