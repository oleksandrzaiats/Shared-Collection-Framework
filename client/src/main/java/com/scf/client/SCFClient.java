package com.scf.client;

import com.scf.client.config.Configuration;
import com.scf.shared.dto.ArtifactDTO;
import com.scf.shared.dto.CollectionDTO;
import com.scf.shared.dto.TokenDTO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        throw new NotImplementedException();
    }

    /**
     * Method for getting collection by Id.
     *
     * @param collectionId identifier of collection.
     * @return collection object.
     */
    public CollectionDTO getCollection(Long collectionId) {
        throw new NotImplementedException();
    }

    /**
     * Method for getting collection by shred key.
     *
     * @param sharedKey shared key for collection.
     * @return collection object.
     */
    public CollectionDTO getCollectionBySharedKey(String sharedKey) {
        throw new NotImplementedException();
    }

    /**
     * Method for creating new collection.
     *
     * @param collection collection to create.
     * @return created collection.
     */
    public CollectionDTO createCollection(CollectionDTO collection) {
        throw new NotImplementedException();
    }

    /**
     * Method for updating collection.
     *
     * @param collection collection to update.
     * @return updated collection.
     */
    public CollectionDTO updateCollection(CollectionDTO collection) {
        throw new NotImplementedException();
    }

    /**
     * Method for deleting collection.
     *
     * @param collection collection to delete.
     */
    public void deleteCollection(CollectionDTO collection) {
        throw new NotImplementedException();
    }

    /**
     * Method for getting all artifacts for current user.
     *
     * @return list of artifacts which are created by current user.
     */
    public List<ArtifactDTO> getAllArtifacts() {
        throw new NotImplementedException();
    }

    /**
     * Method for getting artifact by Id.
     *
     * @param artifactId identifier of artifact.
     * @return artifact object.
     */
    public ArtifactDTO getArtifact(Long artifactId) {
        throw new NotImplementedException();
    }

    /**
     * Method for downloading file from artifact.
     *
     * @param artifact     artifact which contain file.
     * @param outputStream stream for file which will be downloaded.
     */
    public void downloadArtifactFile(ArtifactDTO artifact, FileOutputStream outputStream) {
        throw new NotImplementedException();
    }

    /**
     * Method for creating new artifact.
     *
     * @param artifactName name of artifact.
     * @param inputStream  stream of file to be saved.
     * @return created artifact.
     */
    public ArtifactDTO createArtifact(String artifactName, FileInputStream inputStream) {
        throw new NotImplementedException();
    }

    /**
     * Method for updating artifact.
     *
     * @param artifact artifact to update.
     * @return updated artifact.
     */
    public ArtifactDTO updateArtifact(ArtifactDTO artifact) {
        throw new NotImplementedException();
    }

    /**
     * Method for updating artifact and replace artifact file.
     *
     * @param artifact    artifact to update.
     * @param inputStream stream of file which will replace existing one.
     * @return updated artifact.
     */
    public ArtifactDTO updateArtifactFile(ArtifactDTO artifact, FileInputStream inputStream) {
        throw new NotImplementedException();
    }

    /**
     * Method for deleting artifact.
     *
     * @param artifact artifact to delete.
     */
    public void deleteArtifact(ArtifactDTO artifact) {
        throw new NotImplementedException();
    }

}
