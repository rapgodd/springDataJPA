package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import java.util.List;

@SpringBootTest
class MemberTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void testEntitiy() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member(teamA, "giyeon", 10);
        em.persist(member1);
        Member member2 = new Member(teamB, "Minsu", 12);
        em.persist(member2);

        em.flush();
        em.clear();

        List<Member> selectMFromMemberM = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : selectMFromMemberM) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }

    }

    /**
     * 제대로 생성일 필드와
     * 업데이트일 필드가 추가되었는지
     * 확인하는 테스트입니다.
     */
    @Transactional
    @Test
    public void JpaEventBaseEntityTest() throws Exception {
        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUserName("member2");

        em.flush();
        em.clear();

        Member member1 = memberRepository.findById(member.getId()).get();

        System.out.println("member1.getCreatedDate() = " + member1.getCreatedDate());
        System.out.println("member1.getUpdatedDate() = " + member1.getUpdatedDate());

    }

}