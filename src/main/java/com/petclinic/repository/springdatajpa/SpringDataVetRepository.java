package com.petclinic.repository.springdatajpa;

import com.petclinic.model.Vet;
import com.petclinic.repository.VetRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataVetRepository extends VetRepository, Repository<Vet, Integer> {
}
