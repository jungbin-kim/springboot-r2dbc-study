package kim.jungbin.r2dbc.domain.join;

import static org.springframework.data.relational.core.query.Criteria.where;

import kim.jungbin.r2dbc.domain.join.bean.Team;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TeamRepository {

  private static final String COLUMN_TEAM_ID = "team_id";

  // R2dbcEntityTemplate 을 이용한 DB 접근
  private final R2dbcEntityTemplate template;

  public TeamRepository(R2dbcEntityTemplate template) {
    this.template = template;
  }

  public Mono<Team> findById(String teamId) {
    return template.selectOne(
        Query.query(where(COLUMN_TEAM_ID).is(teamId)),
        Team.class
    );
  }

  public Mono<Team> insert(Team team) {
    return template.insert(team);
  }

  public Mono<Team> update(Team team) {
    return template.update(team);
  }

  public Mono<Integer> deleteById(String teamId) {
    return template.delete(
        Query.query(where(COLUMN_TEAM_ID).is(teamId)),
        Team.class
    );
  }
}
