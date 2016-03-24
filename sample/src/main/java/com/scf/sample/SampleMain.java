package com.scf.sample;

import com.scf.client.AuthClient;
import com.scf.client.config.Configuration;
import com.scf.client.config.ConfigurationFactory;
import com.scf.shared.dto.TokenDTO;
import com.scf.shared.dto.UserDTO;

public class SampleMain {

    public static void main(String[] args) {
        Configuration configuration = ConfigurationFactory.createConfiguration();
        AuthClient authClient = new AuthClient(configuration);

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("test");
        userDTO.setPassword("test");
        userDTO.setName("test");

        testRegister(authClient, userDTO);
        TokenDTO tokenDTO = testLogin(authClient, userDTO);
    }

    private static TokenDTO testLogin(AuthClient authClient, UserDTO userDTO) {
        return authClient.login(userDTO);
    }

    private static UserDTO testRegister(AuthClient authClient, UserDTO userDTO) {
        UserDTO registeredUser = authClient.register(userDTO);
        return registeredUser;
    }
}
