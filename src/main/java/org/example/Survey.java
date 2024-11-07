package org.example;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Survey {

    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;
    private String name;

    public Survey(){
    }
    public Survey(String name){
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String surveyName) {
        this.name = surveyName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
