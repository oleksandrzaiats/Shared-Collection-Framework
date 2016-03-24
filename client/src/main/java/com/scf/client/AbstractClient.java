package com.scf.client;

import com.scf.client.config.Configuration;
import com.scf.client.resource.SCFRestClient;

public abstract class AbstractClient {
    protected Configuration configuration;
    protected SCFRestClient restClient;

    public AbstractClient(Configuration configuration) {
        this.configuration = configuration;
        restClient = new SCFRestClient(configuration);
    }
}
