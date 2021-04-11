package kim.jungbin.r2dbc.domain.person.bean;

import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

// Id가 DB에서 자동 생성되는 경우 Entity
@Getter
@AllArgsConstructor
@Table("tbl_person_2")
public class Person2 {

  private final static Random random = new Random();

  @Id
  private String personId;
  private String name;

  public static Person2 createRandom() {
    // @Id 값이 null 로 가면 DB에서 자동으로 ID를 만들어주지만, R2dbcRepository 의 save 반환 값에서는 생성된 ID를 가져오지 못한다.
    // 따라서 생성한 객체의 Id를 알 수 없다는 단점이 있다.
    return new Person2(null, "test" + random.nextInt(1000));
  }

  public static Person2 updatePerson(String personId, String name) {
    return new Person2(personId, name);
  }

}
