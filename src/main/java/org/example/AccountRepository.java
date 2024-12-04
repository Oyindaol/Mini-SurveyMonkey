package org.example;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

}
