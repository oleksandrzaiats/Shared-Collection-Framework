package com.scf.client;

import com.scf.client.config.Configuration;
import com.scf.client.resource.ResourceMapping;
import com.scf.shared.dto.TokenDTO;
import com.scf.shared.dto.UserDTO;

/**
 * Client class for user authorization and registration.
 */
public class AuthClient extends AbstractClient {

    /**
     * Constructor for AuthClient.
     * @param configuration configuration for client. Must be created with {@link com.scf.client.config.ConfigurationFactory}.
     */
    public AuthClient(Configuration configuration) {
        super(configuration);
    }

    /**
     * Perform login action for user.
     * @param user user to login.
     * @return token to use in {@link SCFClient}.
     */
    public TokenDTO login(UserDTO user) {
        validateBean(user);
        return restClient.executeRequest(ResourceMapping.LOGIN, user);
    }

    /**
     * Registering user.
     * @param user user to register.
     * @return created user with Id.
     */
    public UserDTO register(UserDTO user) {
        validateBean(user);
        return restClient.executeRequest(ResourceMapping.REGISTER, user);
    }
}
