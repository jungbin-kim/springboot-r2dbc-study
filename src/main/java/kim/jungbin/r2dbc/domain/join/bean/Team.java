package kim.jungbin.r2dbc.domain.join.bean;

import java.util.Random;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@AllArgsConstructor
@Table("tbl_team")
public class Team {

  private final static Random random = new Random();

  @Id
  private String teamId;
  private String name;

  public static Team createRandom() {
    return new Team(UUID.randomUUID().toString(), "team" + random.nextInt(1000));
  }

}
