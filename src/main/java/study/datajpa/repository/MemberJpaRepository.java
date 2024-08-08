package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    /**
     * 회원의 나이와 이름을 기준으로 Member를 가져오려면 기존에는 쿼리를 직접 작성행야 했다.
     * 하지만 Spring data Jpa를 사용하면 이러한 문제를 해결해줄수 있다.
     */
    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
        return em.createQuery("select m from Member m where m.userName = :username and m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    /**
     * 순수 JPA를 사용해서
     * 페이징처리를 하는 방법
     * offset : 몇번째부터
     * limit : 몇개를 가져와
     *
     * setFirstResult : 어디서부터 가져올건지 적는다.
     * setMaxResults : 개수를 몇개 가져올건지.
     */
    public List<Member> findByPage(int age, int offset, int limit) {
        return  em.createQuery("select m from Member m where age = : age order by m.userName desc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    /**
     * 순수 JPA로 벌크성 수정처리
     * (압데이트 한번에 모아서 하는것)하고 있다.
     */
    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age+1 where m.age >= : age", Member.class)
                .setParameter("age", age)
                .executeUpdate();
    }

}
