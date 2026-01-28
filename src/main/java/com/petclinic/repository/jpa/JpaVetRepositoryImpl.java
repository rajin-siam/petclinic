package com.petclinic.repository.jpa;

import com.petclinic.model.Vet;
import com.petclinic.repository.VetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class JpaVetRepositoryImpl implements VetRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Vet> findAll() {
        return this.em.createQuery("SELECT distinct vet FROM Vet vet left join fetch vet.specialties ORDER BY vet.lastName, vet.firstName", Vet.class).getResultList();
    }
}
