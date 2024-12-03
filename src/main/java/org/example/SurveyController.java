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

    @Autowired
    private AccountRepository accountRepository;
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

    @GetMapping("/create/{accountId}")
    public String displaySurveyFormOnAcount(@PathVariable Long accountId,Model model) {
        model.addAttribute("accountId", accountId);
        model.addAttribute("survey", new Survey());
        return "createsurveywithaccount";
    }
    @PostMapping("/create/{accountID}")
    public String createSurveyOnAccount(@ModelAttribute Survey survey,@PathVariable Long accountID) {
        surveyRepository.save(survey);
        Account account = accountRepository.findById(accountID).orElseThrow(() -> new RuntimeException("Account not found"));
        account.addSurvey(survey);
        accountRepository.save(account);
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
