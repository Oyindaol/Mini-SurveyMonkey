package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyRepository surveyRepository;
    @GetMapping
    public String displaySurveyForm(Model model) {
        model.addAttribute("survey", new Survey());
        return "createsurvey";
    }
    @PostMapping
    public String createSurvey(@ModelAttribute Survey survey) {
        surveyRepository.save(survey);
        return "redirect:/survey/" + survey.getId() + "/question/create";
    }

    @GetMapping("/getbyid/{id}")
    public String getSurveyById(@PathVariable Long id, Model model) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));
        model.addAttribute("survey", survey);
        return "displaysurvey";
    }

    @GetMapping("/getbyname/{name}")
    public String getSurveyByName(@PathVariable String name, Model model) {
        Survey survey = surveyRepository.findByName(name).orElseThrow(() -> new RuntimeException("Survey not found"));
        model.addAttribute("survey", survey);
        return "displaysurvey";
    }
}
