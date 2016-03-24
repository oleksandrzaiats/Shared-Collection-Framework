package com.scf.server.application.model.dao;

import se.lnu.application.entity.UserEntity;
import se.lnu.application.entity.ArtifactEntity;
import se.lnu.application.entity.CollectionEntity;
import se.lnu.application.security.UserRole;

import java.util.List;

/**
 * Created by olefir on 2016-03-20.
 */
public class InitialValue {

    public static ArtifactEntity getArtifactEntity(UserEntity userEntity) {
        ArtifactEntity artifactEntity = new ArtifactEntity();
        artifactEntity.setName("test");
        artifactEntity.setContentType("test");
        artifactEntity.setFile(new byte[]{'t', 'e', 's', 't'});
        artifactEntity.setFileName("test");
        artifactEntity.setUser(userEntity);

        return artifactEntity;
    }

    public static UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("test");
        userEntity.setLogin("login");
        userEntity.setRole(UserRole.ROLE_USER.toString());
        userEntity.setPassword("test");

        return userEntity;
    }

    public static CollectionEntity getCollectionEntity(UserEntity userEntity, List<ArtifactEntity> artifactEntityList, List<CollectionEntity> collectionEntityList) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.setName("test");
        collectionEntity.setUser(userEntity);
        collectionEntity.setArtifactList(artifactEntityList);
        collectionEntity.setCollectionList(collectionEntityList);
        collectionEntity.setKey("test");

        return collectionEntity;
    }
}
