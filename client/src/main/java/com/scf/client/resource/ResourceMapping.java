package com.scf.client.resource;

import com.scf.shared.dto.TokenDTO;
import com.scf.shared.dto.UserDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public enum ResourceMapping {
    LOGIN("login", HttpMethod.POST, MediaType.APPLICATION_JSON, TokenDTO.class),
    REGISTER("user", HttpMethod.POST, MediaType.APPLICATION_JSON, UserDTO.class),
    ;


    private String urlSuffix;
    private HttpMethod httpMethod;
    private MediaType mediaType;
    private Class<?> responseClass;

    ResourceMapping(String urlSuffix, HttpMethod httpMethod, MediaType mediaType, Class<?> responseClass) {
        this.urlSuffix = urlSuffix;
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
        this.responseClass = responseClass;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public Class<?> getResponseClass() {
        return responseClass;
    }
}
