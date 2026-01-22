package com.petclinic.repository;

import com.petclinic.model.Pet;
import com.petclinic.model.PetType;
import com.petclinic.model.Vet;

import java.util.Collection;
import java.util.List;

public interface VetRepository {

    Collection<Vet> findAll();
}
