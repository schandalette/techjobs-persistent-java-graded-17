package org.launchcode.techjobs.persistent.controllers;

import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("employers")
public class EmployerController {

    @Autowired //allows Spring to inject the repository automatically
    private EmployerRepository employerRepository;

    @RequestMapping("/")
    public String index(Model model) { //get all employers from the database and pass to the employers/index view
        model.addAttribute("employers", employerRepository.findAll());
        return "employers/index";
    }

    @GetMapping("add") //display for adding new employer
    public String displayAddEmployerForm(Model model) {
        model.addAttribute(new Employer());
        return "employers/add";
    }

    @PostMapping("add") // process the form submission for adding new employer. if valid saves to database
    public String processAddEmployerForm(@ModelAttribute @Valid Employer newEmployer, Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "employers/add";
        }

        employerRepository.save(newEmployer);
        return "redirect:";
    }

    @GetMapping("view/{employerId}") //gets employer by ID and displays if found. otherwise, redirects to previous page
    public String displayViewEmployer(Model model, @PathVariable int employerId) {

        Optional<Employer> optEmployer = employerRepository.findById(employerId);
        if (optEmployer.isPresent()) {
            Employer employer = optEmployer.get();
            model.addAttribute("employer", employer);
            return "employers/view";
        } else {
            return "redirect:../";
        }

    }
}
