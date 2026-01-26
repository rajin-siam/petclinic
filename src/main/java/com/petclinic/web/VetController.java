package com.petclinic.web;

import com.petclinic.model.Vets;
import com.petclinic.service.ClinicService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class VetController {

    private final ClinicService clinicService;

    public VetController (ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/vets")
    public String showVetList(Map<String, Object> model) {
        Vets vets = getVets();
        model.put("vets", vets);
        return "vets/vetList";
    }
    @GetMapping(value = "/vets.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public
    Vets showJsonVetList() {
        return getVets();
    }

    @GetMapping(value = "/vets.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Vets showXmlVetList() {
        return getVets();
    }

    private Vets getVets() {
        Vets vets = new Vets();
        vets.getVetList().addAll(this.clinicService.findVets());
        return vets;
    }
}
