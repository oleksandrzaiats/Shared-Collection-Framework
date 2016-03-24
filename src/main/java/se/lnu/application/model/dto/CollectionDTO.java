package se.lnu.application.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class CollectionDTO implements CommonDTO {
    private Long id;
    
    private String name;
    private String key;
    private UserDTO user;

    private List<CollectionDTO> collectionList;
    private List<ArtifactDTO> artifactList;

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

    public List<CollectionDTO> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<CollectionDTO> collectionList) {
        this.collectionList = collectionList;
    }

    @NotNull
    public List<ArtifactDTO> getArtifactList() {
        return artifactList;
    }

    public void setArtifactList(List<ArtifactDTO> artifactList) {
        this.artifactList = artifactList;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    @NotNull
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
