package com.petclinic.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import java.util.*;

@Entity
@Table(name = "vets")
public class Vet extends Person{
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vet_specialities", joinColumns = @JoinColumn(name = "vet_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id"))
    private Set<Specialty> specialities;

    protected Set<Specialty> getSpecialtiesInternal() {
        if(this.specialities == null) {
            this.specialities = new HashSet<>();
        }
        return this.specialities;
    }

    protected void setSpecialtiesInternal(Set<Specialty> specialities) {
        this.specialities = specialities;
    }

    @XmlElement
    public List<Specialty> getSpecialties() {
        List<Specialty> sortedSpecialities = new ArrayList<>(getSpecialtiesInternal());
        PropertyComparator.sort(sortedSpecialities, new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedSpecialities);
    }

    public int getNrOfSpecialties() {
        return getSpecialtiesInternal().size();
    }
    public void addSpecialty(Specialty specialty) {
        getSpecialtiesInternal().add(specialty);
    }

}
