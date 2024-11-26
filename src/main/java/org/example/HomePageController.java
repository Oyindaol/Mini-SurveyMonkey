package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HomePageController {

    @Autowired
    private SurveyRepository surveyRepository;

    @GetMapping
    public String displayHomePage(Model model) {
        model.addAttribute("surveyName", "");
        return "homepage";
    }

    @PostMapping
    public String searchSurvey(@RequestParam("surveyName") String surveyName) {
        Survey survey = surveyRepository.findByName(surveyName).orElseThrow(() -> new RuntimeException("Survey not found"));
        return "redirect:/survey/" + survey.getId() + "/respond";
    }
}
