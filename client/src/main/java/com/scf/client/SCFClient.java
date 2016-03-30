package com.scf.client;

import com.scf.client.config.Configuration;
import com.scf.client.resource.ResourceMapping;
import com.scf.shared.dto.ArtifactDTO;
import com.scf.shared.dto.CollectionDTO;
import com.scf.shared.dto.TokenDTO;
import com.scf.shared.exception.InvalidBeanException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;

import java.io.File;
import java.util.List;

/**
 * Base client for Shared Collection Framework.
 * Contains all framework base features.
 */
public class SCFClient extends AbstractClient {

    private TokenDTO tokenDTO;

    /**
     * Constructor for base client.
     *
     * @param configuration configuration for client. Must be created with {@link com.scf.client.config.ConfigurationFactory}.
     * @param tokenDTO      auth token for current user. Must be received with {@link AuthClient}.
     */
    public SCFClient(Configuration configuration, TokenDTO tokenDTO) {
        super(configuration);
        this.tokenDTO = tokenDTO;
    }

    /**
     * Method for getting all collections for current user.
     *
     * @return list of collections which are created by current user.
     */
    public List<CollectionDTO> getAllCollections() {
        return restClient.executeRequest(tokenDTO, ResourceMapping.COLLECTION_GET_LIST, null);
    }

    /**
     * Method for getting collection by Id.
     *
     * @param collectionId identifier of collection.
     * @return collection object.
     */
    public CollectionDTO getCollection(Long collectionId) {
        validateId(collectionId);
        ResourceMapping collectionGet = ResourceMapping.COLLECTION_GET;
        collectionGet.setUrlParameters(new String[]{collectionId.toString()});
        return restClient.executeRequest(tokenDTO, collectionGet, null);
    }

    /**
     * Method for getting collection by shred key.
     *
     * @param sharedKey shared key for collection.
     * @return collection object.
     */
    public CollectionDTO getCollectionBySharedKey(String sharedKey) {
        if (sharedKey == null || sharedKey.isEmpty()) {
            throw new InvalidBeanException("SharedKey parameter is null or empty.");
        }
        ResourceMapping collectionGet = ResourceMapping.COLLECTION_GET_BY_SHARED_KEY;
        collectionGet.setUrlParameters(new String[]{sharedKey});
        return restClient.executeRequest(tokenDTO, collectionGet, null);
    }

    /**
     * Method for creating new collection.
     *
     * @param collection collection to create.
     * @return created collection.
     */
    public CollectionDTO createCollection(CollectionDTO collection) {
        validateBean(collection);
        return restClient.executeRequest(tokenDTO, ResourceMapping.COLLECTION_POST, collection);
    }

    /**
     * Method for updating collection.
     *
     * @param collection collection to update.
     * @return updated collection.
     */
    public CollectionDTO updateCollection(CollectionDTO collection) {
        validateBean(collection);
        return restClient.executeRequest(tokenDTO, ResourceMapping.COLLECTION_UPDATE, collection);
    }

    /**
     * Method for deleting collection.
     *
     * @param collection collection to delete.
     */
    public void deleteCollection(CollectionDTO collection) {
        validateId(collection.getId());
        ResourceMapping collectionDelete = ResourceMapping.COLLECTION_DELETE;
        collectionDelete.setUrlParameters(new String[]{collection.getId().toString()});
        restClient.executeRequest(tokenDTO, collectionDelete, collection);
    }

    /**
     * Method for getting all artifacts for current user.
     *
     * @return list of artifacts which are created by current user.
     */
    public List<ArtifactDTO> getAllArtifacts() {
        return restClient.executeRequest(tokenDTO, ResourceMapping.ARTIFACT_GET_LIST, null);
    }

    /**
     * Method for getting artifact by Id.
     *
     * @param artifactId identifier of artifact.
     * @return artifact object.
     */
    public ArtifactDTO getArtifact(Long artifactId) {
        validateId(artifactId);
        ResourceMapping artifactGet = ResourceMapping.ARTIFACT_GET;
        artifactGet.setUrlParameters(new String[]{artifactId.toString()});
        return restClient.executeRequest(tokenDTO, artifactGet, null);
    }

    /**
     * Method for downloading file from artifact.
     *
     * @param artifact artifact which contain file.
     * @param file     with path to directory where file will be saved
     * @return downloaded file
     */
    public File downloadArtifactFile(ArtifactDTO artifact, File file) {
        validateId(artifact.getId());
        if (!file.exists()) {
            throw new IllegalArgumentException("Directory does not exists.");
        }
        String absolutePath = file.getAbsolutePath();
        if (absolutePath.endsWith(File.separator)) {
            absolutePath = absolutePath + artifact.getFileName();
        } else {
            absolutePath = absolutePath + File.separator + artifact.getFileName();
        }
        File artifactFile = new File(absolutePath);
        if (artifactFile.exists()) {
            throw new RuntimeException("'" + artifact.getFileName() + "' already exists in '" + file.getAbsolutePath() + "' directory.");
        }

        ResourceMapping downloadFIle = ResourceMapping.ARTIFACT_FILE;
        downloadFIle.setUrlParameters(new String[]{artifact.getId().toString()});
        restClient.downloadFile(tokenDTO, downloadFIle, artifactFile);
        return artifactFile;
    }

    /**
     * Method for creating new artifact.
     *
     * @param artifactName name of artifact.
     * @param file         file to be saved.
     * @return created artifact.
     */
    public ArtifactDTO createArtifact(String artifactName, File file) {
        if (artifactName == null) {
            throw new NullPointerException("Artifact's name parameter is null.");
        }
        if (file == null) {
            throw new NullPointerException("File is null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exists.");
        }
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        LinkedMultiValueMap<String, Object> requestParameterMap = new LinkedMultiValueMap<>();
        requestParameterMap.add("file", fileSystemResource);
        requestParameterMap.add("name", artifactName);

        return restClient.executeMultipartRequest(tokenDTO, ResourceMapping.ARTIFACT_POST, requestParameterMap);
    }

    /**
     * Method for updating artifact's information.
     *
     * @param artifact artifact to update.
     * @return updated artifact.
     */
    public ArtifactDTO updateArtifact(ArtifactDTO artifact) {
        validateBean(artifact);
        return restClient.executeRequest(tokenDTO, ResourceMapping.ARTIFACT_UPDATE, artifact);
    }

    /**
     * Method for updating artifact's file.
     *
     * @param artifact artifact to update.
     * @param file     file which will replace existing one.
     * @return updated artifact.
     */
    public ArtifactDTO updateArtifactFile(ArtifactDTO artifact, File file) {
        validateId(artifact.getId());
        if (file == null) {
            throw new NullPointerException("File is null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exists.");
        }
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        LinkedMultiValueMap<String, Object> requestParameterMap = new LinkedMultiValueMap<>();
        requestParameterMap.add("file", fileSystemResource);

        ResourceMapping artifactUpdateFile = ResourceMapping.ARTIFACT_UPDATE_FILE;
        artifactUpdateFile.setUrlParameters(new String[]{artifact.getId().toString()});
        return restClient.executeMultipartRequest(tokenDTO, artifactUpdateFile, requestParameterMap);
    }

    /**
     * Method for deleting artifact.
     *
     * @param artifact artifact to delete.
     */
    public void deleteArtifact(ArtifactDTO artifact) {
        validateId(artifact.getId());
        ResourceMapping artifactDelete = ResourceMapping.ARTIFACT_DELETE;
        artifactDelete.setUrlParameters(new String[]{artifact.getId().toString()});
        restClient.executeRequest(tokenDTO, artifactDelete, artifact);
    }

}
