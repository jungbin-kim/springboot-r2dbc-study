package kim.jungbin.r2dbc.domain.person;

import kim.jungbin.r2dbc.domain.person.bean.Person;
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
@RequestMapping("/v1/persons")
public class PersonController {

  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  private final PersonRepository personRepository;

  @GetMapping
  public Flux<Person> getAllPersons() {
    return personRepository.findAll();
  }

  @GetMapping(value = "/{personId}")
  public Mono<Person> getPerson(
      @PathVariable String personId
  ) {
    return personRepository.findById(personId);
  }

  @PostMapping
  public Mono<Person> createPerson() {
    return personRepository.save(Person.createRandom());
  }

  @PutMapping(value = "/{personId}")
  public Mono<Person> updatePerson(
      @PathVariable String personId,
      @RequestBody UpdatePersonRequest updatePersonRequest
  ) {
    return personRepository.save(Person.updatePerson(personId, updatePersonRequest.getName()));
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
