package org.example;

import org.springframework.data.repository.CrudRepository;
public interface QuestionRepository extends CrudRepository<Question, Long> {
}
