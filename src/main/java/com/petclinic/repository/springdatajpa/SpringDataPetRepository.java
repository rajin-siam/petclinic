package com.petclinic.repository.springdatajpa;

import com.petclinic.model.Pet;
import com.petclinic.model.PetType;
import com.petclinic.repository.PetRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SpringDataPetRepository extends PetRepository, Repository<Pet, Integer> {

    @Override
    @Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
    List<PetType> findPetTypes();
}
