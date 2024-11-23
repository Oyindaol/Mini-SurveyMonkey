package org.example;

import jakarta.persistence.*;
@Entity
public class Answer {
    @Id
    @GeneratedValue
    private Long id;

    private String surveyAnswer;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Question question;

    public Answer(){}

    public Answer(Question question, String answer){
        this.question = question;
        //Validate answer
        if (!question.validateAnswer(answer)) {
            throw new IllegalArgumentException("Invalid answer for question type: " + question.getQuestionType());
        }
        this.surveyAnswer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurveyAnswer() {
        return surveyAnswer;
    }

    public void setSurveyAnswer(String surveyAnswer) {
        if (question != null && !question.validateAnswer(surveyAnswer)) {
            throw new IllegalArgumentException("Invalid answer for question type: " + question.getQuestionType());
        }
        this.surveyAnswer = surveyAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
