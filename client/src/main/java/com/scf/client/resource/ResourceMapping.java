package com.scf.client.resource;

import com.scf.shared.dto.ArtifactDTO;
import com.scf.shared.dto.CollectionDTO;
import com.scf.shared.dto.TokenDTO;
import com.scf.shared.dto.UserDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.text.MessageFormat;
import java.util.List;

public enum ResourceMapping {
    LOGIN("login", HttpMethod.POST, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<TokenDTO>(){}),
    REGISTER("user", HttpMethod.POST, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<UserDTO>(){}),

    COLLECTION_POST("collection/", HttpMethod.POST, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<CollectionDTO>(){}),
    COLLECTION_GET_LIST("collection/", HttpMethod.GET, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<List<CollectionDTO>>(){}),
    COLLECTION_GET("collection/{0}", HttpMethod.GET, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<CollectionDTO>(){}),
    COLLECTION_GET_BY_SHARED_KEY("collection/?shared_key={0}", HttpMethod.GET, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<CollectionDTO>(){}),
    COLLECTION_UPDATE("collection/", HttpMethod.PUT, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<CollectionDTO>(){}),
    COLLECTION_DELETE("collection/{0}", HttpMethod.DELETE, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<String>(){}),

    ARTIFACT_POST("artifact/", HttpMethod.POST, MediaType.MULTIPART_FORM_DATA, new ParameterizedTypeReference<ArtifactDTO>(){}),
    ARTIFACT_GET_LIST("artifact/", HttpMethod.GET, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<List<ArtifactDTO>>(){}),
    ARTIFACT_GET("artifact/{0}", HttpMethod.GET, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<ArtifactDTO>(){}),
    ARTIFACT_UPDATE("artifact/", HttpMethod.PUT, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<ArtifactDTO>(){}),
    ARTIFACT_DELETE("artifact/{0}", HttpMethod.DELETE, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<String>(){}),
    // TODO
    ARTIFACT_FILE("artifact/{0}/file", HttpMethod.GET, MediaType.APPLICATION_JSON, new ParameterizedTypeReference<String>(){}),
    ;


    private String urlSuffix;
    private HttpMethod httpMethod;
    private MediaType mediaType;
    private ParameterizedTypeReference responseClass;
    private String[] urlParameters;

    ResourceMapping(String urlSuffix, HttpMethod httpMethod, MediaType mediaType, ParameterizedTypeReference responseClass) {
        this.urlSuffix = urlSuffix;
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
        this.responseClass = responseClass;
    }

    public String getUrlSuffix() {
        if(urlParameters == null || urlParameters.length == 0) {
            return urlSuffix;
        } else {
            MessageFormat urlFormat = new MessageFormat(urlSuffix);
            return urlFormat.format(urlParameters);
        }
    }

    public void setUrlParameters(String[] urlParameters) {
        this.urlParameters = urlParameters;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public ParameterizedTypeReference getResponseClass() {
        return responseClass;
    }
}
