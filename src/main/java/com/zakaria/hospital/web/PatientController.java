package com.zakaria.hospital.web;

import com.zakaria.hospital.entities.Patient;
import com.zakaria.hospital.repositories.PatientRepository;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class PatientController {
    private PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/index")
    public String index(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "4") int size,
            @RequestParam(name = "keyword", defaultValue = "") String keyword
    ) {
        Page<Patient> pagePatient = patientRepository.findByNameContains(keyword, PageRequest.of(page, size));
        model.addAttribute("patients",pagePatient.getContent());
        model.addAttribute("pages", new int[pagePatient.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("searchKey", keyword);
        return "patients";
    }


    @GetMapping("/delete")
    public String delete(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page)
    {
        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/formPatient")
    public String formPatient(Model model)
    {
        model.addAttribute("patient", new Patient());
     return "formPatient";
    }
    @PostMapping("/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindResult)
    {
        if(bindResult.hasErrors()) {
            return "formPatient";
        }
        log.info(patient.getName());
        log.info(String.valueOf(patient.getScore()));
        log.info(String.valueOf(patient.getBirthdate()));
        log.info(String.valueOf(patient.isSick()));
        patientRepository.save(patient);
        return "redirect:/index";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam(name = "id") Long id)
    {
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "editPatient";
    }
    @PostMapping("/editPatient")
    public String editPatient(@RequestParam(name = "id") Long id, @Valid Patient patient, BindingResult bindResult) {
        if(bindResult.hasErrors()) {
            return "editPatient";
        }
        Patient patient1 = patientRepository.findById(id).get();
        patient1.setBirthdate(patient.getBirthdate());
        patient1.setName(patient.getName());
        patient1.setSick(patient.isSick());
        patient1.setScore(patient.getScore());
        patientRepository.save(patient);
        return "redirect:/index";
    }
}
