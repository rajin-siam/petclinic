package com.petclinic.service;

import com.petclinic.model.*;

import java.util.Collection;

public interface ClinicService {

    Collection<PetType> findPetTypes();

    Owner findOwnerById(int id);

    Pet findPetById(int id) ;

    void savePet(Pet pet);

    void saveVisit(Visit visit);

    Collection<Vet> findVets();

    void saveOwner(Owner owner);

    Collection<Owner> findOwnerByLastName(String lastName);

    Collection<Visit> findVisitsByPetId(int petId);
}
