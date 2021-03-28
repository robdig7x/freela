package com.prox.reservas.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.prox.reservas.entities.Sala;
import com.prox.reservas.enums.TipoSala;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalaDTO {
	
	private Long id;
//	@NotEmpty
//	@NotNull(message = "Nome não pode ser nulo.")
//	@Size(min = 2, message = "O Nome deve possuir no minimo 2 Caracteres")
	private String nome;
	private int lotacao;
	private TipoSala tipo;
	private List<PersonDTO> pessoas = new ArrayList<>();
	
	public SalaDTO(Sala sala) {
		if (sala != null)
			BeanUtils.copyProperties(sala, this);
	}

	public Sala converter() {
		return new Sala(id, nome, lotacao, tipo, null);
	}
	
	public static List<SalaDTO> converter(List<Sala> salas) {
		return salas.stream().map(SalaDTO::new).collect(Collectors.toList());
	}
}
