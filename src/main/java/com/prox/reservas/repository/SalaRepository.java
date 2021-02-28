package com.prox.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prox.reservas.entities.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	Sala findByNome(String nome);
}
