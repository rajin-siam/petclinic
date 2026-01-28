package com.petclinic.repository.jpa;

import com.petclinic.model.Visit;
import com.petclinic.repository.VisitRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaVisitRepository implements VisitRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Visit visit) {
        if(visit.getId()==null){
            this.em.persist(visit);
        } else {
            this.em.merge(visit);
        }
    }

    @Override
    public List<Visit> findByPetId(Integer petId) {
        Query query = this.em.createQuery("SELECT v FROM  Visit v WHERE v.pet.id=:id", Visit.class);
        query.setParameter("id", petId);
        return query.getResultList();
    }
}
