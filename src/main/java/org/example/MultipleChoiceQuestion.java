package org.example;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceQuestion extends Question {

    @ElementCollection
    private List<String> options;

    public MultipleChoiceQuestion() {
        super();
        this.setType(QuestionType.MULTIPLE_CHOICE);
    }

    public MultipleChoiceQuestion(Survey survey, String questionText, List<String> options) {
        super(survey, questionText);
        this.setType(QuestionType.MULTIPLE_CHOICE);
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
