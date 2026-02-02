package com.petclinic.repository.springdatajpa;

import com.petclinic.model.Visit;
import com.petclinic.repository.VisitRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataVisitRepository extends VisitRepository, Repository<Visit, Integer> {
}
