package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("survey/{surveyId}")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;


    @GetMapping("/respond")
    public String displaySurveyQuestions(@PathVariable Long surveyId, Model model) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyId));
        model.addAttribute("survey", survey);
        model.addAttribute("answer", new Answer());
        return "answersurvey";
    }

    @PostMapping("/respond")
    public String submitAnswers(@RequestParam Map<String, String> answers) {
        AtomicLong surveyid = new AtomicLong(1);
        answers.forEach((key, value) -> {
            if (key.startsWith("answers[")) {
                // Extract question ID from the key
                String questionIdStr = key.substring(8, key.indexOf("]"));
                Long questionId = Long.parseLong(questionIdStr);
                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));


                // Create and save the answer
                Answer answer = new Answer();
                answer.setQuestion(question);

                try {
                    answer.setSurveyAnswer(value);
                } catch (IllegalArgumentException e){
                    throw new InvalidInputException(e.getMessage());
                }
                question.addAnswer(answer);
                answerRepository.save(answer);
                surveyid.set(question.getSurvey().getId());
            }
        });
        return "redirect:/survey/getbyid/" + surveyid;
    }
}
