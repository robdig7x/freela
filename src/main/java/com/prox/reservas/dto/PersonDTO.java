package com.prox.reservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDTO {
	
	private Long id;
	
	private String nome;
	
	private SalaDTO sala;
	
	private String sobrenome;
		
	public boolean estaNaSala(Long idSala) {
		return this.sala != null && idSala != null && this.sala.getId().equals(idSala);
	}
}
