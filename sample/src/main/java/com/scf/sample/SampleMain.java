package com.scf.sample;

import com.scf.client.AuthClient;
import com.scf.client.SCFClient;
import com.scf.client.config.Configuration;
import com.scf.client.config.ConfigurationFactory;
import com.scf.shared.dto.CollectionDTO;
import com.scf.shared.dto.TokenDTO;
import com.scf.shared.dto.UserDTO;

import java.util.List;

public class SampleMain {

    public static void main(String[] args) {
        Configuration configuration = ConfigurationFactory.createConfiguration();
        AuthClient authClient = new AuthClient(configuration);

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("test");
        userDTO.setPassword("testtest");
        userDTO.setName("test");

//        testRegister(authClient, userDTO);
        TokenDTO tokenDTO = testLogin(authClient, userDTO);
        SCFClient scfClient = new SCFClient(configuration, tokenDTO);
        CollectionDTO collection = new CollectionDTO();
        collection.setName("new_cooll");
//        scfClient.createCollection(collection);
        List<CollectionDTO> allCollections = scfClient.getAllCollections();
        CollectionDTO collectionDTO = scfClient.getCollection(9L);
        CollectionDTO collectionDTO1 = scfClient.getCollectionBySharedKey("cc28df8f-3524-4fbe-8004-c835cd4323b8");
        collectionDTO.setName("new_changed_name");
        collectionDTO1 = scfClient.updateCollection(collectionDTO);
        scfClient.deleteCollection(collectionDTO1);
        collectionDTO1 = scfClient.getCollection(collectionDTO.getId());

    }

    private static TokenDTO testLogin(AuthClient authClient, UserDTO userDTO) {
        return authClient.login(userDTO);
    }

    private static UserDTO testRegister(AuthClient authClient, UserDTO userDTO) {
        UserDTO registeredUser = authClient.register(userDTO);
        return registeredUser;
    }
}
