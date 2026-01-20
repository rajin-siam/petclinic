package com.petclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "specialities")
public class Speciality extends NamedEntity{

}
