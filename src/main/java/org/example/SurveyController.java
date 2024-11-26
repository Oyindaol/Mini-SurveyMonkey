package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/{surveyId}/charts")
    public String viewSurveyCharts(@PathVariable Long surveyId, Model model) {
        System.out.println("Serving viewcharts.html for surveyId: " + surveyId);
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("Survey not found"));
        model.addAttribute("survey", survey);
        model.addAttribute("surveyId", surveyId);
        return "viewcharts";
    }

    @GetMapping("/{id}/charts/data")
    @ResponseBody
    public Map<String, Object> getSurveyChartData(@PathVariable Long id) {
        System.out.println("Serving viewcharts.html for survey8Id: " + id);
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));
        Map<String, Object> chartData = new HashMap<>();

        for (Question question : survey.getQuestions()) {
            // Calculate and save statistics
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
                    questionData.put("answers", question.getAnswers().stream()
                            .map(Answer::getSurveyAnswer)
                            .toList());
                    break;
            }

            // Add the grouped question data to the main chartData map
            chartData.put("question_" + question.getId(), questionData);
        }

        return chartData;
    }

}
