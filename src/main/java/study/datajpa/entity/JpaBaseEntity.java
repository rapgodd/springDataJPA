package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 모든 테이블에 이 엔티티가
 * 등록되고 수정된 시간 핃드를 추가 해준다.
 *
 * 순수 JPA로 하는 방법이다.
 * Spring Data Jpa로 하는 방법은 따로 있다.
 */
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        LocalDateTime now = LocalDateTime.now();
        updatedDate = now;
    }
}
