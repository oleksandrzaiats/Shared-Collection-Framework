package com.scf.server.application.model.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "artifact")
@Table(name = "artifact")
public class ArtifactEntity implements CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "fileName")
    private String fileName;

    @NotNull
    @NotEmpty
    @Column(name = "contentType")
    private String contentType;

    @Lob
    @Column(name = "file")
    private byte[] file;

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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }
       
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public UserEntity getUser() {
        return user;
    }
    
    public void setUser(UserEntity user) {
        this.user = user;
    }
}
