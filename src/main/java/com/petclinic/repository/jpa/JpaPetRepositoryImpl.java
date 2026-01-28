package com.petclinic.repository.jpa;

import com.petclinic.model.Pet;
import com.petclinic.model.PetType;
import com.petclinic.repository.PetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaPetRepositoryImpl implements PetRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<PetType> findPetTypes() {
     //   return this.em.createQuery("SELECT ptype FROM PetType pytpe ORDER BY pytpe.name").getResultList();
        return this.em.createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name").getResultList();
    }

    @Override
    public Pet findById(int id) {
        return this.em.find(Pet.class, id);
    }

    @Override
    public void save(Pet pet) {
        if(pet.getId() == null) {
            this.em.persist(pet);
        } else {
            this.em.merge(pet);
        }
    }
}
