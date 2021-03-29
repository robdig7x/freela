package com.prox.reservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDTO {
	
	private String id;
	
	private String nome;
	
	private String salaId;
	
	private String sobrenome;
		
	public boolean estaNaSala(String idSala) {
		return this.salaId != null && idSala != null && this.salaId.equals(idSala);
	}
}
