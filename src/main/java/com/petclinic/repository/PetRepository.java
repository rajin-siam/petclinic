package com.petclinic.repository;

import com.petclinic.model.Pet;
import com.petclinic.model.PetType;

import java.util.List;

public interface PetRepository {
    List<PetType> findPetTypes();
    Pet findById(int id);
    void save(Pet pet);
}
