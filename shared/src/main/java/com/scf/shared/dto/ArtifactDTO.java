package com.scf.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ArtifactDTO implements CommonDTO {
    private Long id;
    @NotNull
    @Size(min = 1, max = 255)
    private String name;
    private String fileName;
    private String contentType;
    @JsonIgnore
    private byte[] fileBytes;
    private UserDTO user;

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

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
    
    @NotNull
    @Size(min = 1, max = 255)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
    
    @NotNull
    public UserDTO getUser() {
        return user;
    }
}
