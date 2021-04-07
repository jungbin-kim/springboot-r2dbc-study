package kim.jungbin.r2dbc.domain.join;


import kim.jungbin.r2dbc.domain.join.bean.Team;
import lombok.Getter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/teams")
public class TeamController {

  private final TeamRepository teamRepository;

  public TeamController(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  @GetMapping(value = "/{teamId}")
  public Mono<Team> getTeam(
      @PathVariable String teamId
  ) {
    return teamRepository.findById(teamId);
  }

  @PostMapping
  public Mono<Team> createTeam() {
    return teamRepository.insert(Team.createRandom());
  }

  @PutMapping(value = "/{teamId}")
  public Mono<Team> updateTeam(
      @PathVariable String teamId,
      @RequestBody UpdateTeamRequest updateTeamRequest
  ) {
    return teamRepository.update(updateTeamRequest.getTeam(teamId));
  }

  @DeleteMapping(value = "/{teamId}")
  public Mono<Integer> deleteTeam(
      @PathVariable String teamId
  ) {
    return teamRepository.deleteById(teamId);
  }


  @Getter
  private static class UpdateTeamRequest {

    private String name;

    public Team getTeam(String teamId) {
      return new Team(teamId, name);
    }
  }

}
