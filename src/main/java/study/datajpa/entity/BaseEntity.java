package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 * 스프링 데이터 JPA를 사용해서 엔티티에
 * 수정일과 등록일 필드를 추가하는 방법
 *
 * 만약 어느 테이블은 시간만 필요하고 어느 테이블은 시간과 작성다 다 필요하다면?
 * 그러면 시간은 따로 클래스 만들고 baseEntity에서 extends 한다.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    /**
     * 등록자, 수정자 필드
     * jpa application에 설정 추가하여야 한다.
     */
    @CreatedBy
    @Column(updatable = false )
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
