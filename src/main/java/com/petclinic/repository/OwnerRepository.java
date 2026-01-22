package com.petclinic.repository;

import com.petclinic.model.Owner;
import java.util.Collection;

public interface OwnerRepository {

    Collection<Owner> findByLastName(String lastName);
    Owner findById(int id);
    void save(Owner owner);
}
