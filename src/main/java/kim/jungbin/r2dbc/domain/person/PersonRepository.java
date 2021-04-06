package kim.jungbin.r2dbc.domain.person;

import kim.jungbin.r2dbc.domain.person.bean.Person;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends R2dbcRepository<Person, String> {
  // 구현체 SimpleR2dbcRepository

}
