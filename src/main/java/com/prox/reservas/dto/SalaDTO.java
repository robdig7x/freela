package com.prox.reservas.dto;

import java.util.ArrayList;
import java.util.List;

import com.prox.reservas.enums.TipoSala;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalaDTO {
	
	private Long id;
	private String nome;
	private int lotacao;
	private TipoSala tipo;
	private List<PersonDTO> pessoas = new ArrayList<>();
	
}
