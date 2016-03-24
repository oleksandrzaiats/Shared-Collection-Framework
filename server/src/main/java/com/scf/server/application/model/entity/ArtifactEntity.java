package com.scf.server.application.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "artifact")
@Table(name = "artifact")
public class ArtifactEntity implements CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "contentType")
    private String contentType;

    @Column(name = "file")
    private byte[] file;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    @NotNull
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @NotNull(message="Artifact name can not be Null")
    @Size(min = 1, max = 260, message="Artifact name should contain from 1 to 260 characters")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFile() {
        return file;
    }
    
    @NotNull
    public void setFile(byte[] file) {
        this.file = file;
    }

    @NotNull(message="Artifact name can not be Null")
    @Size(min = 1, max = 260, message="File name should contain from 1 to 260 characters")
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

    @NotNull
    public UserEntity getUser() {
        return user;
    }
    
    public void setUser(UserEntity user) {
        this.user = user;
    }
}
