package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.launchcode.techjobs.persistent.models.JobData;

import java.util.HashMap;

/**
 * Created by LaunchCode
 */
@Controller //MVC controller
@RequestMapping(value = "list") //all request mapping inside this controller will start with /list
public class ListController {

    @Autowired //injects instance of repository
    private JobRepository jobRepository;

    @Autowired //injects instance of repository
    private EmployerRepository employerRepository;

    @Autowired //injects instance of repository
    private SkillRepository skillRepository;

    static final HashMap<String, String> columnChoices = new HashMap<>();

    public ListController () {  //column choices map

        columnChoices.put("all", "All");
        columnChoices.put("employer", "Employer");
        columnChoices.put("skill", "Skill");

    }

    @RequestMapping("") //display the list page is triggered when the user visits /list
    public String list(Model model) {
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "list";
    }

    @RequestMapping("jobs")//filter and display jobs
    public String listJobsByColumnAndValue(Model model, @RequestParam String column, @RequestParam String value) { //@RequestParam annotations extract URL query params
        Iterable<Job> jobs; //iterate over each job...for each loop
        if (column.equalsIgnoreCase("all")){ //displays all jobs
            jobs = jobRepository.findAll();
            model.addAttribute("title", "All Jobs");
        } else { //displays jobs using column value selected
            jobs = JobData.findByColumnAndValue(column, value, jobRepository.findAll());
            model.addAttribute("title", "Jobs with " + columnChoices.get(column) + ": " + value);
        }
        model.addAttribute("jobs", jobs); //job list is added to the model and displayed in the "list-jobs" template

        return "list-jobs";
    }
}
