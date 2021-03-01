package com.prox.reservas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prox.reservas.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
	List<Person> findByNomeContainingIgnoreCase(String nome);
}
