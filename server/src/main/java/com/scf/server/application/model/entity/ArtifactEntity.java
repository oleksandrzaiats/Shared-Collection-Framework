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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtifactEntity that = (ArtifactEntity) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        return contentType != null ? contentType.equals(that.contentType) : that.contentType == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        return result;
    }
}
