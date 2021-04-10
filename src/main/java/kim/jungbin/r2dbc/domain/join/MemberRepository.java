package kim.jungbin.r2dbc.domain.join;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import kim.jungbin.r2dbc.domain.join.bean.Member;
import kim.jungbin.r2dbc.domain.join.bean.Team;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MemberRepository {

  private static final String COLUMN_MEMBER_ID = "member_id";

  private final R2dbcEntityTemplate template;

  private final MemberTableMapper memberTableMapper;

  public MemberRepository(R2dbcEntityTemplate template) {
    // `DatabaseClient client` 를 inject 해서 client.sql() 을 사용하는 방법도 있다.
    this.template = template;
    this.memberTableMapper = new MemberTableMapper();
  }

  public Flux<Member> findAll() {
    return template.getDatabaseClient()
        .sql("SELECT a.member_id, a.name, b.team_id, b.name "
            + "FROM tbl_member a INNER JOIN tbl_team b ON a.team_id = b.team_id ")
        .map(memberTableMapper::apply)
        .all();
  }

  public Mono<Member> findById(String memberId) {
    return template.getDatabaseClient()
        .sql("SELECT a.member_id, a.name, b.team_id, b.name "
            + "FROM tbl_member a INNER JOIN tbl_team b ON a.team_id = b.team_id "
            + "WHERE a.member_id = :memberId")
        .bind("memberId", memberId)
        .map(memberTableMapper::apply)
        .one();
  }

  public Mono<Member> insert(Member member) {
    // `template.insert(member)`: R2dbcEntityTemplate template 의 insert 를 사용할 경우, Member 객체 내부에 Team 객체가 있어서 MappingR2dbcConverter 에서 `Nested entities are not supported` 에러가 발생한다.
    return template.getDatabaseClient()
        .sql("INSERT INTO tbl_member (`member_id`,`name`,`team_id`) VALUES "
            + "( :memberId , :memberName , :teamId )")
        .bind("memberId", member.getMemberId())
        .bind("memberName", member.getName())
        .bind("teamId", member.getTeam().getTeamId())
        .fetch()
        .rowsUpdated()
        .then(findById(member.getMemberId()))
        ;
  }

  public Mono<Member> update(Member member) {
    return template.getDatabaseClient()
        .sql("UPDATE tbl_member SET `name` = :memberName, `team_id` = :teamId "
            + "WHERE member_id = :memberId")
        .bind("memberId", member.getMemberId())
        .bind("memberName", member.getName())
        .bind("teamId", member.getTeam().getTeamId())
        .fetch()
        .rowsUpdated()
        .then(findById(member.getMemberId()));
  }

  public Mono<Integer> delete(String memberId) {
    return template.delete(
        Query.query(where(COLUMN_MEMBER_ID).is(memberId)),
        Member.class
    );
  }

  private static class MemberTableMapper implements BiFunction<Row, Object, Member> {

    @Override
    public Member apply(Row row, Object o) {
      String memberId = row.get("member_id", String.class);
      String memberName = row.get("name", String.class);
      String teamId = row.get("team_id", String.class);
      String teamName = row.get("name", String.class);

      Team team = new Team(teamId, teamName);

      return Member.builder()
          .memberId(memberId)
          .name(memberName)
          .team(team)
          .build();
    }
  }

}
