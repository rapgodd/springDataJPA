package study.datajpa.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

//<엔티티 타입, id 타입>

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{
    List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

    List<Member> findHelloBy();

    @Query("select m from Member m where m.userName = :username and m.age = :age ")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.userName from Member m")
    List<String> findAllUserName();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDto> findAllMemberDto();

    @Query("select m from Member m where m.userName in :usernames")
    List<Member> findByUserNames(@Param("usernames") List<String> usernames);

    Optional<Member> findOptionalByUserName(String userName);

    List<Member> findMemberByUserName(String userName);

    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * 스프링 데이터 JPA로 벌크성 수정처리
     * (압데이트 한번에 모아서 하는것)하고 있다.
     * 순수 JPA에서 .executeUpdate() 이것처럼
     * 여기서는 @Modifying 사용해야 한다.
     * (clearAutomatically = true) 이 부분은 영속성 컨텍스트를 clear 하는 상황인것이다.
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= : age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team")
    List<Member> findAllTeamMember();

    /**
     * 메서드 이름으로 JPQL 자동 생성하고 싶은데
     * fetch join 까지 하고 싶을때
     * 엔티티 그래프를 사용해서 문제를 해결한다.
     */
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    /**
     * JPQL 짜고 fetch join도 함께
     */
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    /**
     * 다른 예시
     */
    @EntityGraph(attributePaths = {"team"})
    Member findByUserName(@Param("username") String username);

    /**
     * 이 메서드가 오직 read만 할때 JPA가 Hibernate에게
     * 자신은 영속성 컨텍스트를 사용하지 않을거닌까
     * 제거하라고 힌트를 주는것
     * (성능 최적화)
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUserName(@Param("username") String username);
}
