package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    EntityManager em;


    @Test
    public void testMember() {
        Member member = new Member("임성팔");

        Member saveedMember = memberRepository.save(member);
        Member member1 = memberRepository.findById(member.getId()).get();

        assertThat(member1).isEqualTo(saveedMember);
        assertThat(member1.getUserName()).isEqualTo(member.getUserName());
    }

    @Test
    public void basicCRUD() {

        Member member1 = new Member("한국인");
        Member member2 = new Member("미국인");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(findMember1);
        memberRepository.delete(findMember2);

        long count1 = memberRepository.count();
        assertThat(count1).isZero();
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aa", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> aaa = memberRepository.findByUserNameAndAgeGreaterThan("aaa", 2);

        assertThat(aaa.size()).isEqualTo(1);
        assertThat(aaa.get(0).getAge()).isEqualTo(10);
        assertThat(aaa.get(0).getUserName()).isEqualTo("aaa");

    }

    @Test
    public void findHelloBy() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aa", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);


        List<Member> helloBy = memberRepository.findHelloBy();

        assertThat(helloBy.size()).isEqualTo(2);
    }

    @Test
    public void testQuery() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aa", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> aa = memberRepository.findUser("aa", 20);
        assertThat(aa.get(0)).isEqualTo(member2);
    }

    @Test
    public void UserNamesTest() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aa", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> aa = memberRepository.findByUserNames(Arrays.asList("aa", "aaa"));
        assertThat(aa.size()).isEqualTo(2);


    }

    @Test
    public void UserNamesOptionalTest() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aa", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Optional<Member> aa = memberRepository.findOptionalByUserName("aa");
        assertThat(aa).isEqualTo(Optional.of(member2));

    }

    @Test
    public void UserListNullTest() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aa", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> aa = memberRepository.findMemberByUserName("aa");

        System.out.println("aa.size() = " + aa.size());

    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        /**
         * <SPRING DATA JPA를 가지고 페이징>
         * pageRequest 만들어야한다
         * pqge를 0번부터 시작하고 3개씩 가져온다. 라는 의미
         *
         */
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        int age = 10;
        int offset = 0;
        int limit = 3;

        /**
         * JPA 인터페이스 보면 나이하고 Pageable 파라미터로 집어 넣었다.
         * 반환타입이 Page이면 TatalCount도 자동으로 구해준다.
         * 마지막으로 Page를 유지하면서 Member를 Dto로 바꾸는 방법을 알려준다.
         */
        Page<Member> byAge = memberRepository.findByAge(age, pageRequest);
        byAge.map(m -> new MemberDto(m.getId(), m.getUserName(), null));


        /**
         * 안에 있는 데이터만 꺼내오기
         */
        List<Member> content = byAge.getContent();
        long totalElements = byAge.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(byAge.getNumber()).isEqualTo(0);
        assertThat(byAge.getTotalPages()).isEqualTo(2);
        assertThat(byAge.isFirst()).isTrue();
        assertThat(byAge.hasNext()).isTrue();

    }

    @Test
    public void findMemberLazy() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member(teamA, "member1", 10);
        Member member2 = new Member(teamB, "member2", 10);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member.getUserName() = " + member.getUserName());
            System.out.println("member.getTeam().getClass() = " + member.getTeam());
        }

    }

    /**
     * 만약에 메서드나 서비스가 조회용일때에는
     * 영속성 컨텍스트가 필요없고
     * 이를 JPA가 구현체인 hibernate에게 힌트를 줘서 알려줄 수 있음.
     */
    @Test
    public void queryHint() {
        //given
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        Member member1 = memberRepository.findById(member.getId()).get();
        /**
         * 이 경우 member1을 영속성 컨텍스트에 담을 이유가 없다.
         */

    }

    @Test
    public void projections() {
        Team teamA = new Team("TeamA");
        em.persist(teamA);

        Member m1 = new Member(teamA,"m1",0);
        Member m2 = new Member(teamA,"m2",0);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        List<UsernameOnly> result = memberRepository.findProjectionsByUserName("m1");

        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly);
        }
    }

    @Test
    public void DTOtest() {
        Team teamA = new Team("TeamA");
        em.persist(teamA);

        Member m1 = new Member(teamA,"m1",0);
        Member m2 = new Member(teamA,"m2",0);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        List<Member> m11 = memberRepository.findMember1("m1");

    }





}