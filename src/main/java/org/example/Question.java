package org.example;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Survey survey;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    private String surveyQuestion;

    //For numeric question
    private Integer minValue;
    private Integer maxValue;

    //For Multiple Choice Question
    @ElementCollection
    private List<String> options;

    public enum QuestionType {
        OPEN_ENDED,
        NUMERIC,
        MULTIPLE_CHOICE
    }

    public Question(){}

    public Question(Survey survey, String question, QuestionType questionType) {
        this.survey = survey;
        this.surveyQuestion = question;
        this.questionType = questionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(String question) {
        this.surveyQuestion = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    // Validation for answers
    public boolean validateAnswer(String answer) {
        switch (this.questionType) {
            case OPEN_ENDED:
                return true; // Open-ended answers need no validation
            case NUMERIC:
                try {
                    int numericAnswer = Integer.parseInt(answer);
                    return numericAnswer >= this.minValue && numericAnswer <= this.maxValue;
                } catch (NumberFormatException e) {
                    return false; // Invalid number format
                }
            case MULTIPLE_CHOICE:
                return this.options != null && this.options.contains(answer);
            default:
                throw new IllegalArgumentException("Unknown question type: " + this.questionType);
        }
    }


}
