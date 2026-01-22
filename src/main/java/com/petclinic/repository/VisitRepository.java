package com.petclinic.repository;

import com.petclinic.model.Visit;
import java.util.List;

public interface VisitRepository {
    void save(Visit visit);
    List<Visit> findByPetId(Integer petId);
}
