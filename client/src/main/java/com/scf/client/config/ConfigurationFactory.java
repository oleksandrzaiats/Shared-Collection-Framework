package com.scf.client.config;

public class ConfigurationFactory {
    public static Configuration createConfiguration() {
        return new ConfigurationV1();
    }
}
