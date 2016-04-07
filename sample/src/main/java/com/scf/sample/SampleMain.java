package com.scf.sample;

import com.scf.client.AuthClient;
import com.scf.client.SCFClient;
import com.scf.client.config.Configuration;
import com.scf.client.config.ConfigurationFactory;
import com.scf.shared.dto.ArtifactDTO;
import com.scf.shared.dto.CollectionDTO;
import com.scf.shared.dto.TokenDTO;
import com.scf.shared.dto.UserDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class SampleMain {

    public static void main(String[] args) throws FileNotFoundException {
        Configuration configuration = ConfigurationFactory.createConfiguration();
        AuthClient authClient = new AuthClient(configuration);

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("zzzzzz");
        userDTO.setPassword("111111");
        userDTO.setName("test");

//        testRegister(authClient, userDTO);
        TokenDTO tokenDTO = testLogin(authClient, userDTO);
        SCFClient scfClient = new SCFClient(configuration, tokenDTO);
        CollectionDTO collection = new CollectionDTO();
        collection.setName("new_cooll");
        scfClient.createCollection(collection);
        List<CollectionDTO> allCollections = scfClient.getAllCollections();
        CollectionDTO collectionDTO = scfClient.getCollection(allCollections.get(0).getId());
        CollectionDTO collectionDTO1 = scfClient.getCollectionBySharedKey(allCollections.get(0).getKey());
        collectionDTO.setName("new_changed_name");
        collectionDTO1 = scfClient.updateCollection(collectionDTO);

        File file = new File("E:\\University\\5 course Sweden\\ASD\\FileFramework\\server\\src\\main\\resources\\application.properties");
        ArtifactDTO new_artifact = scfClient.createArtifact("new_artifact", file);
        new_artifact.setName("changed_One");
        new_artifact = scfClient.updateArtifact(new_artifact);

        collectionDTO1.setArtifactList(Arrays.asList(new_artifact));
        collectionDTO1 = scfClient.updateCollection(collectionDTO1);

        File file1 = new File("E:\\University\\5 course Sweden\\ASD\\FileFramework\\server\\src\\main\\");
        scfClient.downloadArtifactFile(new_artifact, file1);

        file = new File("E:\\University\\5 course Sweden\\ASD\\FileFramework\\server\\build.gradle");
        new_artifact = scfClient.updateArtifactFile(new_artifact, file);
        scfClient.downloadArtifactFile(new_artifact, file1);
        scfClient.deleteArtifact(new_artifact);
        scfClient.deleteCollection(collectionDTO1);

    }

    private static TokenDTO testLogin(AuthClient authClient, UserDTO userDTO) {
        return authClient.login(userDTO);
    }

    private static UserDTO testRegister(AuthClient authClient, UserDTO userDTO) {
        UserDTO registeredUser = authClient.register(userDTO);
        return registeredUser;
    }
}
