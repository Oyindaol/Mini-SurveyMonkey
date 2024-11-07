package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("survey/{surveyId}/question/{questionId}/answer")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/create")
    public String displayAnswerForm(@PathVariable Long surveyId, @PathVariable Long questionId, Model model) {
        model.addAttribute("surveyId", surveyId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("answer", new Answer());
        return "submitanswer";
    }

    @PostMapping("/create")
    public String createAnswer(@PathVariable Long surveyId, @PathVariable Long questionId, @ModelAttribute Answer answer) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Survey not found"));
        answer.setQuestion(question);
        question.addAnswer(answer);
        answerRepository.save(answer);
        return "redirect:/survey/getbyid/" + surveyId;
    }
}
