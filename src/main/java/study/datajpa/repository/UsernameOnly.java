package study.datajpa.repository;


/**
 * 만약 회원엔티티 전체가 아니라
 * 회원의 이름만 가지고 오고 싶다면?
 */
public interface UsernameOnly {

    String getUsername();

}
