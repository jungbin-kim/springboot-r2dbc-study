package kim.jungbin.r2dbc.domain.person.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Random;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor // sql에 포함되지 않는 isNew가 추가되면서 DB에서 Person을 조회했을 때 Person 객체를 만들지 못하는 문제가 발생하여 추가
@AllArgsConstructor
@Table("tbl_person") // db table 과 매핑되는 객체 이름이 다를 경우 명시
public class Person implements Persistable<String> {

  private final static Random random = new Random();
  /* ID Generation
   SpringData R2DBC는 ID를 사용하여 엔티티를 식별. 엔티티의 ID는 SpringData의 @Id Annotation 필요.
   DB에 ID 컬럼에 Auto Increase 가 설정된 경우, 생성 된 값은 데이터베이스에 삽입 된 후 엔터티에 설정됩니다.

   SpringData R2DBC는 엔티티 식별자 값이 초기 값으로 설정되어있는 경우 ID 컬럼의 값을 삽입지 않음.
   초기 값 => primitive type 유형의 경우 0, Long과 같은 숫자 참조 타입인 경우 null

   https://docs.spring.io/spring-data/r2dbc/docs/current-SNAPSHOT/reference/html/#r2dbc.entity-persistence.id-generation
  */
  // R2dbcRepository 방식에서 @Id 어노테이션 없을 경우 save 가 수행되지만, 에러 발생.
  // => @Id 어노테이션 추가 시 `Failed to update table [tbl_person]` 발생.
  // => @Id 유무로 insert 와 update가 구분되는데 이를 위해서 Persistable 을 구현하여 isNew 값을 지정해준다.
  @Id
  private String personId;
  private String name;

  @JsonIgnore // response body(json 형태) 에 포함되지 않도록 함.
  @Transient // sql에 포함되지 않도록함
  private boolean isNew; // insert 일 때는 true, update 일 때는 false.

  public static Person createRandom() {
    return new Person(UUID.randomUUID().toString(), "test" + random.nextInt(1000), true);
  }

  public static Person updatePerson(String personId, String name) {
    return new Person(personId, name, false);
  }

  @JsonIgnore
  @Override
  public String getId() {
    return personId;
  }

  // save 는 insert와 update 둘로 나뉨
  // true일 경우, 구현체 SimpleR2dbcRepository 에서 insert 로 판단함
  @JsonIgnore
  @Override
  public boolean isNew() {
    return isNew;
  }
}
