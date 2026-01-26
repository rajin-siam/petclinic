package com.petclinic.web;

import com.petclinic.model.PetType;
import com.petclinic.service.ClinicService;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

public class PetTypeFormatter implements Formatter<PetType> {

    private final ClinicService clinicService;

    public PetTypeFormatter(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @Override
    public PetType parse(String text, Locale locale) throws ParseException {
        Collection<PetType> petTypes = clinicService.findPetTypes();
        for (PetType petType : petTypes) {
            if (petType.getName().equals(text)) {
                return petType;
            }
        }
        throw  new ParseException("Type not found" + text, 0);
    }

    @Override
    public String print(PetType petType, Locale locale) {
        return petType.getName();
    }
}
