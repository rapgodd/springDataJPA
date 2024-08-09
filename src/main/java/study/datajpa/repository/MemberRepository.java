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

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
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

    /**
     * 스프링 데이터 jpa를 사용홰서 엔티티 전체중 일부만 가져오고 싶으면
     * 즉, 예를들어 유저 엔티티중 유저 이름만 가져오고 싶으면.
     * 리포지토리에 인터페이스를 하나 만들고 원하는 필드를 넣는다.
     * 그리고 data jpa에 메서드를 만들고 반환타입을 인터페이스 타입으로 넣는다.
     */
    List<UsernameOnly> findProjectionsByUserName(@Param("username") String userName);

    /**
     * 네이티브 쿼리를 하고 싶으면 뒤에 nativeQuery = true 를 추가하면 된다.
     * 이름은 아무렇게나 해도 상관없다. 어차피 쿼리를 커스텀해서 날릴거기 때문에.
     * 추천 X
     */
    @Query(value = "select * from member where userName = ?", nativeQuery = true)
    Member findByNativeQuery(String userName);

    /**
     * 네이티브 쿼리를 사용하면서
     * 원하는 것만 뽑고싶을때 & 동적쿼리 아닐때
     * 사용가능한 것이다.
     */
    @Query(value = "select m.id as id," +
            " m.userName, t.name as teamName " +
            "from Member m left join Team t",
            countQuery = "select count(*) from Member",
            nativeQuery = true)
    Page<findByNativeProjection> findBtNativeProj(Pageable pageable);


}
