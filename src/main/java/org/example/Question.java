package org.example;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //Numeric Questions Statistics
    private Integer mean;
    private Integer median;
    private Integer standardDeviation;
    private Integer largestAnswer;
    private Integer smallestAnswer;

    //Multiple Choice Questions Statistics
    @ElementCollection
    @CollectionTable(name = "question_options_count", joinColumns = @JoinColumn(name = "question_id"))
    @MapKeyColumn(name = "option")
    @Column(name = "count")
    private Map<String, Integer> optionsCount;

    @ElementCollection
    @CollectionTable(name = "question_options_percentage", joinColumns = @JoinColumn(name = "question_id"))
    @MapKeyColumn(name = "option")
    @Column(name = "percentage")
    private Map<String, Double> optionsPercentage;



    public Question(){}

    public Question(Survey survey, String question, QuestionType questionType) {
        this.survey = survey;
        this.surveyQuestion = question;
        this.questionType = questionType;
        this.options = new ArrayList<>();
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

    public int getMean() {
        return mean;
    }

    public void setMean(int mean) {
        this.mean = mean;
    }

    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    public int getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(int standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public int getLargestAnswer() {
        return largestAnswer;
    }

    public void setLargestAnswer(int largestAnswer) {
        this.largestAnswer = largestAnswer;
    }

    public int getSmallestAnswer() {
        return smallestAnswer;
    }

    public void setSmallestAnswer(int smallestAnswer) {
        this.smallestAnswer = smallestAnswer;
    }

    public Map<String, Integer> getOptionsCount() {
        return optionsCount;
    }

    public void setOptionsCount(Map<String, Integer> optionsCount) {
        this.optionsCount = optionsCount;
    }

    public Map<String, Double> getOptionsPercentage() {
        return optionsPercentage;
    }

    public void setOptionsPercentage(Map<String, Double> optionsPercentage) {
        this.optionsPercentage = optionsPercentage;
    }

    // Calculate the statistics for a question
    public void calculateAndSaveStatistics() {
        switch (this.questionType) {
            case NUMERIC:
                List<Integer> numericAnswers = this.answers.stream()
                        .map(answer -> Integer.parseInt(answer.getSurveyAnswer()))
                        .toList();

                if (!numericAnswers.isEmpty()) {
                    Map<String, Object> stats = AnswerStatistics.calculateNumericStats(this.answers);
                    this.mean = (int) Math.round((double) stats.get("Mean"));
                    this.median = (int) Math.round((double) stats.get("Median"));
                    this.standardDeviation = (int) Math.round((double) stats.get("Standard Deviation"));
                    this.largestAnswer = (int) stats.get("Max");
                    this.smallestAnswer = (int) stats.get("Min");
                }
                break;

            case MULTIPLE_CHOICE:
                Map<String, Integer> counts = AnswerStatistics.calculateMultipleChoiceCounts(this.answers, this.options);
                Map<String, Double> percentages = AnswerStatistics.calculateMultipleChoicePercentages(this.answers, this.options);

                this.optionsCount = counts;
                this.optionsPercentage = percentages;
                break;

            default:
                break;
        }
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
