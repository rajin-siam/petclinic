package com.petclinic.web;

import com.petclinic.model.Owner;
import com.petclinic.service.ClinicService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class OwnerController {

    private static final String VIEWS_OWNERS_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private final ClinicService clinicService;

    public OwnerController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/owners/new")
    public String initCreationForm(Map<String, Object> model) {
        Owner owner = new Owner();
        model.put("owner", owner);
        return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/owners/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
        }

        this.clinicService.saveOwner(owner);
        return "redirect:/owners/" + owner.getId();
    }

    @GetMapping(value = "/owners/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("owner", new Owner());
        return "owners/findOwners";
    }

    @GetMapping(value = "/owners")
    public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {

        if (owner.getLastName() == null) {
            owner.setLastName("");
        }

        var results = this.clinicService.findOwnerByLastName(owner.getLastName());
        if (results.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");
            return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
        } else if (results.size() == 1) {
            owner = results.iterator().next();
            return "redirect:/owners/" + owner.getId();
        } else {
            model.put("selections", results);
            return "owners/ownersList";
        }
    }

    @PostMapping(value = "/owners/{ownerId}/edit")
    public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Map<String, Object> model) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        model.put("owner", owner);
        return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
    }

    @GetMapping(value = "/owners/{ownerId}/edit")
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId) {
        if (result.hasErrors()) {
            return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
        }

        owner.setId(ownerId);
        this.clinicService.saveOwner(owner);
        return "redirect:/owners/{ownerId}";
    }

    @GetMapping("/owners/{ownerId}")
    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(this.clinicService.findOwnerById(ownerId));
        return mav;
    }


}
