package org.example;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OPEN_ENDED")
public class OpenEndedQuestion extends Question {
    public OpenEndedQuestion() {
        super();
        this.setType(QuestionType.OPEN_ENDED);
    }

    public OpenEndedQuestion(Survey survey, String questionText) {
        super(survey, questionText);
        this.setType(QuestionType.OPEN_ENDED);
    }
}
