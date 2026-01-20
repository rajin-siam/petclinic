package com.petclinic.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Vets {

    private List<Vet> vetList;

    @XmlElement(name = "vet")
    public List<Vet> getVetList() {
        if(this.vetList == null) {
            vetList = new ArrayList<>();
        }
        return vetList;
    }
}
