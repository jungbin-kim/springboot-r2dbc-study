package kim.jungbin.r2dbc.domain.join.bean;

import java.util.Random;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@Table(value = "tbl_member")
public class Member {

  private final static Random random = new Random();

  @Id
  private String memberId;
  private String name;
  // 테스트: 내부 객체 있을 경우
  private Team team;

  public static Member getMemberForInsert(String memberName, String teamId) {
    Team team = new Team(teamId, null);
    return Member.builder()
        .memberId(UUID.randomUUID().toString())
        .name(memberName)
        .team(team)
        .build();
  }

  public static Member getMemberForUpdate(String memberId, String memberName, String teamId) {
    Team team = new Team(teamId, null);
    return Member.builder()
        .memberId(memberId)
        .name(memberName)
        .team(team)
        .build();
  }

}
