package kim.jungbin.springboot.domain.person;

import kim.jungbin.springboot.domain.person.bean.Person2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v2/persons")
public class Person2Controller {

    private final Person2Service person2Service;

    public Person2Controller(Person2Service person2Service) {
        this.person2Service = person2Service;
    }

    @PostMapping("/bulk1")
    public Mono<List<Person2>> createRandomPersonList1() {

        return person2Service.createRandomPersonListWithoutScheduler();
    }

    @PostMapping("/bulk2")
    public Mono<List<Person2>> createRandomPersonList2() {

        return person2Service.createRandomPersonListForTestTransaction();
    }

}
