package kim.jungbin.r2dbc.domain.person;

import kim.jungbin.r2dbc.domain.person.bean.Person2;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Person2Repository extends R2dbcRepository<Person2, String> {
  // 구현체 SimpleR2dbcRepository

}
