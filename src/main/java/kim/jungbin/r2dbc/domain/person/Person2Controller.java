package kim.jungbin.r2dbc.domain.person;

import kim.jungbin.r2dbc.domain.person.bean.Person2;
import lombok.Getter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v2/persons")
public class Person2Controller {

  private final Person2Repository personRepository;

  public Person2Controller(Person2Repository personRepository) {
    this.personRepository = personRepository;
  }
  
  @GetMapping
  public Flux<Person2> getAllPersons() {
    return personRepository.findAll();
  }

  @GetMapping(value = "/{personId}")
  public Mono<Person2> getPerson(
      @PathVariable String personId
  ) {
    return personRepository.findById(personId);
  }

  @PostMapping
  public Mono<Person2> createPerson() {

    return personRepository.save(Person2.createRandom());
  }

  @PutMapping(value = "/{personId}")
  public Mono<Person2> updatePerson(
      @PathVariable String personId,
      @RequestBody UpdatePersonRequest updatePersonRequest
  ) {
    return personRepository.save(Person2.updatePerson(personId, updatePersonRequest.getName()));
  }

  @DeleteMapping(value = "/{personId}")
  public Mono<Void> deletePerson(
      @PathVariable String personId
  ) {
    return personRepository.deleteById(personId);
  }

  @Getter
  private static class UpdatePersonRequest {

    private String name;
  }
}
