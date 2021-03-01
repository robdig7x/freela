package com.prox.reservas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prox.reservas.entities.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	List<Sala> findByNomeContainingIgnoreCase(String keyword);
}
