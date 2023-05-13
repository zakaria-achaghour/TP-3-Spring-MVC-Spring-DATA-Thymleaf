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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/user/index")
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


    @GetMapping("/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page)
    {
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/admin/formPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formPatient(Model model)
    {
        model.addAttribute("patient", new Patient());
     return "formPatient";
    }
    @PostMapping("/admin/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
        return "redirect:user/index?keyword="+patient.getName();
    }

    @GetMapping("/admin/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(Model model, @RequestParam(name = "id") Long id)
    {
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "editPatient";
    }
    @PostMapping("/admin/editPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editPatient(@Valid Patient patient, BindingResult bindResult) {
        if(bindResult.hasErrors()) {
            return "editPatient";
        }
        patientRepository.save(patient);
        return "redirect:/user/index?keyword="+patient.getName();
    }
    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";
    }
}
