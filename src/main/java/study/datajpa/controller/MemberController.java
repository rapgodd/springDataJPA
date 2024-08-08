package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUserName();
    }

    /**
     * 페이징을 하는 메서드이다.
     * Pageable 인터페이스를 파라미터로 받은 다음에
     * Spring Data Jpa 기본 메서드 끝에 집어넣으면 된다.
     * <p>
     * 이거를 페이징을 하고 싶으면 여기서
     * URL에 파라미터 바인딩만 하면 된다.
     * EX)
     * /members?page=0&size=3&sort=id,desc
     * 추가로 page가 0부터 시작하는 것이 마음에 들지 않는다면 yml설정을 통해 바꿀수 있다.
     */
    @GetMapping("/members")
    public Page<MemberDto> findAll(@PageableDefault(size = 12, sort = "userName") Pageable pageable) {
        Page<Member> all = memberRepository.findAll(pageable);
        Page<MemberDto> map = all.map(m -> new MemberDto(m.getId(), m.getUserName(), null));
        return map;
    }

    @PostConstruct
    public void init() {
        for (int i=0; i<100; i++) {
            memberRepository.save(new Member("userA"));
        }
    }

}
