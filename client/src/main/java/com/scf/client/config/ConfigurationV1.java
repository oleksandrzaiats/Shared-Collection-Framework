package com.scf.client.config;

public class ConfigurationV1 implements Configuration {

    private final String SUFFIX = "api/v1.0/";

    public String getServerUrl() {
        return SERVER_URL + SUFFIX;
    }
}
