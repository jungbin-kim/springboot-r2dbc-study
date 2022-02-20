package kim.jungbin.springboot.domain.person;

import kim.jungbin.springboot.common.transaction.ReactiveTransactional;
import kim.jungbin.springboot.domain.person.bean.Person2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
@Service
public class Person2Service {

    private Person2Mapper person2Mapper;

    public Person2Service(Person2Mapper person2Mapper) {
        this.person2Mapper = person2Mapper;
    }

    // Transaction 정상 작동
    @ReactiveTransactional
    public Mono<List<Person2>> createRandomPersonListWithoutScheduler() {
        return Mono.fromCallable(() -> {
                Person2 p1 = Person2.createRandom();
                log.info("p1");
                person2Mapper.insertPerson(p1);
                return p1;
            })
            .flatMap(p1 -> Mono.fromCallable(() -> {
                         Person2 p2 = Person2.createRandom();
                         log.info("p2");
                         int result = person2Mapper.insertPerson(p2);
                         // 반드시 에러가 발생한다.
                         if (result == 1) {
                             throw new RuntimeException();
                         }
                         return List.of(p1, p2);
                     })
            );
    }

    // Transaction 비정상 작동
    @ReactiveTransactional
    public Mono<List<Person2>> createRandomPersonListForTestTransaction() {
        return Mono.fromCallable(() -> {
                Person2 p1 = Person2.createRandom();
                log.info("p1");
                person2Mapper.insertPerson(p1);
                return p1;
            })
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap(p1 -> Mono.fromCallable(() -> {
                             Person2 p2 = Person2.createRandom();
                             log.info("p2");
                             int result = person2Mapper.insertPerson(p2);

                             if (result == 1) {
                                 throw new RuntimeException();
                             }
                             return List.of(p1, p2);
                         })
                         .subscribeOn(Schedulers.boundedElastic())
            );
    }
}
