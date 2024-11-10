package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("survey/{surveyId}/question")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @GetMapping("/create")
    public String displayQuestionFrom(@PathVariable Long surveyId, Model model) {
        model.addAttribute("surveyId", surveyId);
        model.addAttribute("question", new Question());
        return "createquestion";
    }

    @PostMapping("/create")
    public String createQuestion(@PathVariable Long surveyId, @ModelAttribute Question question) {

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        question.setSurvey(survey);
        survey.addQuestion(question);

        questionRepository.save(question);
        surveyRepository.save(survey);

        Long questionId = question.getId();

        return "redirect:/survey/getbyid/" + surveyId;
    }
}
