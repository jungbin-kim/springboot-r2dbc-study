package kim.jungbin.r2dbc.domain.join;

import kim.jungbin.r2dbc.domain.join.bean.Member;
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
@RequestMapping("/v1/members")
public class MemberController {

  private final MemberRepository memberRepository;

  public MemberController(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @GetMapping(value = "/{memberId}")
  public Mono<Member> getMember(
      @PathVariable String memberId
  ) {
    return memberRepository.findById(memberId);
  }

  @PostMapping
  public Mono<Member> createMember(
      @RequestBody CreateUpdateMemberRequest createMemberRequest
  ) {
    return memberRepository.insert(Member.getMemberForInsert(
        createMemberRequest.getMemberName(),
        createMemberRequest.getTeamId()
        )
    );
  }

  @PutMapping(value = "/{memberId}")
  public Mono<Member> updateMember(
      @PathVariable String memberId,
      @RequestBody CreateUpdateMemberRequest updateMemberRequest
  ) {
    return memberRepository.update(Member.getMemberForUpdate(
        memberId,
        updateMemberRequest.getMemberName(),
        updateMemberRequest.getTeamId())
    );
  }

  @DeleteMapping(value = "/{memberId}")
  public Mono<Integer> deleteTeam(
      @PathVariable String memberId
  ) {
    return memberRepository.delete(memberId);
  }

  @Getter
  private static class CreateUpdateMemberRequest {

    private String memberName;
    private String teamId;

  }
}
