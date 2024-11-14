package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","name"})
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    private String test;

    public Team(String name) {
        this.name = name;
    }
}
