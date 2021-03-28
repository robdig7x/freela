package com.prox.reservas.repository;

import org.springframework.stereotype.Repository;

import com.prox.reservas.dto.PersonDTO;
import com.prox.reservas.firebase.service.FireStoreRepository;

@Repository
public class PessoaRepository implements FireStoreRepository<PersonDTO>{
}
