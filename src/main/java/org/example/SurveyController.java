package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.ff4j.FF4j;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private AccountRepository accountRepository;
  @Autowired
    private FF4j ff4j;

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
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
        model.addAttribute("accountId", accountId);
        model.addAttribute("survey", new Survey());
        return "createsurveywithaccount";
    }
    @PostMapping("/create/{accountID}")
    public String createSurveyOnAccount(@ModelAttribute Survey survey,@PathVariable Long accountID) {
        surveyRepository.save(survey);
        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountID));
        account.addSurvey(survey);
        accountRepository.save(account);
        return "redirect:/survey/" + survey.getId() + "/question/create";
    }

    @GetMapping("/getbyid/{id}")
    public String getSurveyById(@PathVariable Long id, Model model) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + id));
        model.addAttribute("survey", survey);
        return "displaysurvey";
    }

    @GetMapping("/getbyname/{name}")
    public String getSurveyByName(@PathVariable String name, Model model) {
        Survey survey = surveyRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with name: " + name));
        model.addAttribute("survey", survey);
        return "displaysurvey";
    }

    @GetMapping("/{surveyId}/charts")
    public String viewSurveyCharts(@PathVariable Long surveyId, Model model) {
        if(!ff4j.check("CHART_GENERATOR")){
            throw new FeatureDisabledException("Chart generator feature is disabled");
        }
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyId));

        model.addAttribute("survey", survey);

        if (!survey.isClosed()) {
            model.addAttribute("message", "Please close the survey to view the chart(s)");
            return "displaysurvey";
        }

        model.addAttribute("surveyId", surveyId);
        return "viewcharts";
    }

    @GetMapping("/{id}/charts/data")
    @ResponseBody
    public Map<String, Object> getSurveyChartData(@PathVariable Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + id));
        Map<String, Object> chartData = new HashMap<>();

        for (Question question : survey.getQuestions()) {
            question.calculateAndSaveStatistics();

            Map<String, Object> questionData = new HashMap<>();
            questionData.put("questionName", question.getSurveyQuestion());

            switch (question.getQuestionType()) {
                case NUMERIC:
                    questionData.put("type", "numeric");
                    questionData.put("statistics", Map.of(
                            "mean", question.getMean(),
                            "median", question.getMedian(),
                            "stdDev", question.getStandardDeviation(),
                            "min", question.getSmallestAnswer(),
                            "max", question.getLargestAnswer()
                    ));
                    break;

                case MULTIPLE_CHOICE:
                    questionData.put("type", "multiple_choice");
                    questionData.put("percentages", question.getOptionsPercentage());
                    break;

                case OPEN_ENDED:
                    questionData.put("type", "open_ended");
                    questionData.put("responses", question.getAnswers().stream()
                            .map(Answer::getSurveyAnswer)
                            .toList());
                    break;
            }

            chartData.put("question_" + question.getId(), questionData);
        }

        return chartData;
    }

    @PostMapping("/{surveyId}/close")
    public String closeSurvey(@PathVariable Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyId));
        survey.setClosed(true);
        surveyRepository.save(survey);
        return "redirect:/survey/getbyid/" + surveyId;
    }
}
