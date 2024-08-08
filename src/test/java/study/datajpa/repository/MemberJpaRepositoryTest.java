package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)//이렇게 하면 로그로 쿼리가 보인다. //테스트를 해도 DB에 실제로 적용이 된다. //공부하는 과정에서는 필요 실무에서는 삭제
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void save() {

        Member member = new Member("최기연");
        Member save = memberJpaRepository.save(member);


        Member member1 = memberJpaRepository.find(save.getId());
        assertThat(member1.getId()).isEqualTo(save.getId());

    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aa", 20);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> aaa = memberJpaRepository.findByUsernameAndAgeGreaterThen("aaa", 2);

        assertThat(aaa.size()).isEqualTo(1);
        assertThat(aaa.get(0).getAge()).isEqualTo(10);
        assertThat(aaa.get(0).getUserName()).isEqualTo("aaa");

    }


}