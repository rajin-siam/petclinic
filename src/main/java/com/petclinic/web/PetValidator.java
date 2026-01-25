package com.petclinic.web;

import com.petclinic.model.Pet;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PetValidator implements Validator {

    private static final String REQUIRED = "required";
    @Override
    public boolean supports(Class<?> clazz) {
        return Pet.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Pet pet = (Pet) target;
        String name = pet.getName();
        // Name validation
        if(!StringUtils.hasLength(name)){
            errors.rejectValue("name", REQUIRED, REQUIRED);
        }

        // Type validation
        if (pet.isNew() && pet.getType() == null) {
            errors.rejectValue("type", REQUIRED, REQUIRED);
        }

        // Birth date validation
        if (pet.getBirthDate() == null) {
            errors.rejectValue("birthDate", REQUIRED, REQUIRED);
        }
    }
}
