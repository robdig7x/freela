package com.prox.reservas.dto;

import com.prox.reservas.enums.TipoSala;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalaDTO {
	
	private String id;
	private String nome;
	private int lotacao;
	private TipoSala tipo;
	
}
