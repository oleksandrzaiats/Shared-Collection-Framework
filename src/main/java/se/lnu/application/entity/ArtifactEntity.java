package se.lnu.application.entity;

import javax.persistence.*;

@Entity(name = "artifact")
@Table(name = "artifact")
public class ArtifactEntity implements CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
