package com.scf.client.resource;

import com.scf.shared.dto.*;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;

public enum ResourceMapping {
    LOGIN("login", HttpMethod.POST, MediaType.APPLICATION_JSON, TokenDTO.class),
    REGISTER("user", HttpMethod.POST, MediaType.APPLICATION_JSON, UserDTO.class),

    COLLECTION_POST("collection/", HttpMethod.POST, MediaType.APPLICATION_JSON, CollectionDTO.class),
    COLLECTION_GET_LIST("collection/", HttpMethod.GET, MediaType.APPLICATION_JSON, CollectionDTO[].class),
    COLLECTION_GET("collection/{0}", HttpMethod.GET, MediaType.APPLICATION_JSON, CollectionDTO.class),
    COLLECTION_GET_BY_SHARED_KEY("collection/?shared_key={0}", HttpMethod.GET, MediaType.APPLICATION_JSON, CollectionDTO.class),
    COLLECTION_UPDATE("collection/", HttpMethod.PUT, MediaType.APPLICATION_JSON, CollectionDTO.class),
    COLLECTION_DELETE("collection/{0}", HttpMethod.DELETE, MediaType.APPLICATION_JSON, String.class),

    ARTIFACT_POST("artifact/", HttpMethod.POST, MediaType.MULTIPART_FORM_DATA, ArtifactDTO.class),
    ARTIFACT_GET_LIST("artifact/", HttpMethod.GET, MediaType.APPLICATION_JSON, ArtifactDTO[].class),
    ARTIFACT_GET("artifact/{0}", HttpMethod.GET, MediaType.APPLICATION_JSON, ArtifactDTO.class),
    ARTIFACT_UPDATE("artifact/", HttpMethod.PUT, MediaType.APPLICATION_JSON, ArtifactDTO.class),
    ARTIFACT_DELETE("artifact/{0}", HttpMethod.DELETE, MediaType.APPLICATION_JSON, String.class),
    ARTIFACT_FILE("artifact/{0}/file", HttpMethod.GET, MediaType.APPLICATION_JSON, byte[].class),
    ARTIFACT_UPDATE_FILE("artifact/{0}/file", HttpMethod.POST, MediaType.MULTIPART_FORM_DATA, ArtifactDTO.class),;


    private String urlSuffix;
    private String httpMethod;
    private String mediaType;
    private Class responseClass;
    private String[] urlParameters;

    ResourceMapping(String urlSuffix, String httpMethod, String mediaType, Class responseClass) {
        this.urlSuffix = urlSuffix;
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
        this.responseClass = responseClass;
    }

    public String getUrlSuffix() {
        if (urlParameters == null || urlParameters.length == 0) {
            return urlSuffix;
        } else {
            MessageFormat urlFormat = new MessageFormat(urlSuffix);
            return urlFormat.format(urlParameters);
        }
    }

    public void setUrlParameters(String[] urlParameters) {
        this.urlParameters = urlParameters;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Class getResponseClass() {
        return responseClass;
    }
}
