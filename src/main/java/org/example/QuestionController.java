package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

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
    public String createQuestion(@PathVariable Long surveyId, @ModelAttribute Question question, @RequestParam("choices")
            String questionType, @RequestParam(value = "multipleChoiceInput", required = false) String answerChoice) {

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        question.setQuestionType(Question.QuestionType.valueOf(questionType));
        if (questionType.equals("MULTIPLE_CHOICE")){
            String[] temp = answerChoice.split(",");
            question.setOptions(new ArrayList<>());
            for(String s:temp){
                question.getOptions().add(s);
            }
        }
        question.setSurvey(survey);
        survey.addQuestion(question);

        questionRepository.save(question);
        surveyRepository.save(survey);

        Long questionId = question.getId();

        return "redirect:/survey/getbyid/" + surveyId;
    }
}
