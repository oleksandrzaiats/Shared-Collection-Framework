package com.scf.server.application.model.entity;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "collection")
@Table(name = "collection")
public class CollectionEntity implements CommonEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "name")
    private String name;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "shared_key")
    private String key;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ArtifactEntity> artifactList;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<CollectionEntity> collectionList;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }

    public List<ArtifactEntity> getArtifactList() {
        return artifactList;
    }

    public void setArtifactList(List<ArtifactEntity> artifactList) {
        this.artifactList = artifactList;
    }

    public List<CollectionEntity> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<CollectionEntity> collectionList) {
        this.collectionList = collectionList;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
