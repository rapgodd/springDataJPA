package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 * Spring Data Jpa 를 사용하면서 엔티티에
 * String Id 를 사용하고 싶다면
 * save를 호출할때 merge가 호출됨으로
 * 이렇게 되지 않도록 Persistable을 상속받아야 함.
 * 그리고 id에 값이 있어도 새로운 객체라는 것을 메서드로
 * 커스텀해서 알려줘야 함.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Item implements Persistable<String> {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

}
