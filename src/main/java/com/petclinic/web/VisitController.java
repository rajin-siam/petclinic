package com.petclinic.web;

import com.petclinic.model.Pet;
import com.petclinic.model.Visit;
import com.petclinic.service.ClinicService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class VisitController {

    private final ClinicService clinicService;

    public VisitController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        Visit visit = new Visit();
        pet.addVisit(visit);
        return visit;
    }

    @GetMapping(value = "/owners/*/pets/{petId}/visits/new")
    public String initNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model) {
        return "visits/createOrUpdateVisitForm";
    }

    @PostMapping(value = "/owners/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        }

        this.clinicService.saveVisit(visit);
        return "redirect:/owners/{ownerId}";
    }

    @GetMapping(value = "/owners/*/pets/{petId}/visits")
    public String showVisits(@PathVariable int petId, Map<String, Object> model) {
        model.put("visits", this.clinicService.findPetById(petId).getVisits());
        return "visitList";
    }
}
