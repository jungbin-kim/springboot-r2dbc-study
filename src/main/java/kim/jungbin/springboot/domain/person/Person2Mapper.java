package kim.jungbin.springboot.domain.person;

import kim.jungbin.springboot.domain.person.bean.Person2;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface Person2Mapper {

    int insertPerson(Person2 person2);
}
