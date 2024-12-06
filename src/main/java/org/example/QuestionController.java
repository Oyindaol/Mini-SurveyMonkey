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
        Survey survey = surveyRepository.findById(surveyId)
                        .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyId));
        model.addAttribute("surveyId", surveyId);
        model.addAttribute("question", new Question());
        return "createquestion";
    }

    @PostMapping("/create")
    public String createQuestion(@PathVariable Long surveyId, @ModelAttribute Question question, @RequestParam("choices")
            String questionType, @RequestParam(value = "multipleChoiceInput", required = false) String answerChoice) {

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyId));

        try{
            question.setQuestionType(Question.QuestionType.valueOf(questionType));
        } catch (IllegalArgumentException e){
            throw new InvalidInputException("Invalid question type: " + questionType);
        }
        if(question.getQuestionType() == Question.QuestionType.MULTIPLE_CHOICE) {
            if (answerChoice == null || answerChoice.trim().isEmpty()) {
                throw new InvalidInputException("Multiple choice options are required for MULTIPLE_CHOICE question type.");
            }
            String[] options = answerChoice.split(",");
            question.setOptions(new ArrayList<>());
            for (String option : options) {
                question.getOptions().add(option.trim());
            }
        }

        question.setSurvey(survey);
        survey.addQuestion(question);

        questionRepository.save(question);
        surveyRepository.save(survey);

        return "redirect:/survey/getbyid/" + surveyId;
    }
}
