package org.example;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NUMERIC")
public class NumericQuestion extends Question {

    private Integer minValue;
    private Integer maxValue;

    public NumericQuestion() {
        super();
        this.setType(QuestionType.NUMERIC);
    }

    public NumericQuestion(Survey survey, String question, Integer minValue, Integer maxValue) {
        super(survey, question);
        this.setType(QuestionType.NUMERIC);
        this.minValue = minValue;
        this.maxValue = maxValue;

    }
}
