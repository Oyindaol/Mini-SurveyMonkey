package org.example;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
public interface SurveyRepository extends CrudRepository<Survey, Long> {
    Optional<Survey> findById(long id);
    Optional<Survey> findByName(String name);
}
