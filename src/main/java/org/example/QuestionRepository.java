package org.example;

import org.springframework.data.repository.CrudRepository;
import java.util.*;
public interface QuestionRepository extends CrudRepository<Question, Long> {
    Optional<Question>  findById(long id);
}
