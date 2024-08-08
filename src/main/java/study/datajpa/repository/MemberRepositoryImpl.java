package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import java.util.List;

/**
 * 사용자 정의 리포지토리.
 * Spring Data Jpa에서 제공하는 기능말고
 * 다른 리포지토리 기능을 구현하고 싶을때 어떻게 하여야 하는가?
 * 1. 인터페이스를 만들고 해당기능 정의
 * 2. 그 인터페이스를 상속하는 리포 생성후에 해당 기능 구현
 * 3. Spring Data Jpa에서 인터페이스 extends하면 바로 커스텀 메서드 사용가능.
 *
 * // 주의사항 //
 * 복잡한 메서드들은 사용자 정의 리포지토리를 사용하는 것보다
 * 그냥 따로 리포지토리를 만드는 것이 더 좋다.
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
