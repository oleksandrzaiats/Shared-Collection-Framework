package se.lnu.application.dto;

import java.util.List;

public class CollectionDTO implements CommonDTO {
    long id;
    String name;
    String key;

    List<CollectionDTO> collectionList;
    List<ArtifactDTO> artifactList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public List<ArtifactDTO> getArtifactList() {
        return artifactList;
    }

    public void setArtifactList(List<ArtifactDTO> artifactList) {
        this.artifactList = artifactList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
