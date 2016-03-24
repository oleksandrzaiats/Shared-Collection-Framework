package se.lnu.application.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ArtifactDTO implements CommonDTO {
    private Long id;
    private String name;
    private String fileName;
    private String contentType;
    @JsonIgnore
    private byte[] fileBytes;
    private UserDTO user;

    @NotNull
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(min = 1, max = 260)
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
    @Size(min = 1, max = 260)
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
