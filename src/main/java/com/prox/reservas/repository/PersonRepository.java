package com.prox.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prox.reservas.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
	Person findByNome(String nome);
}
