package com.prox.reservas.repository;

import org.springframework.stereotype.Repository;

import com.prox.reservas.dto.SalaDTO;
import com.prox.reservas.firebase.service.FireStoreRepository;

@Repository
public class SalaRepository implements FireStoreRepository<SalaDTO> {
}
