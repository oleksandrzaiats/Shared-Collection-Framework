package com.scf.client.resource;

import com.scf.client.config.Configuration;
import com.scf.shared.dto.CommonDTO;
import com.scf.shared.dto.TokenDTO;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static com.scf.shared.utils.StringUtils.AUTH_HEADER_NAME;

public class SCFRestClient extends RestTemplate {

    private final Configuration configuration;
    // 30 seconds timeout
    private final int TIMEOUT = 30000;

    public SCFRestClient(Configuration configuration) {
        super();
        this.configuration = configuration;
        configureRestClient();
    }

    private void configureRestClient() {
        List<HttpMessageConverter<?>> messageConverters = getMessageConverters();
        HttpMessageConverter<Object> jackson = new MappingJackson2HttpMessageConverter();
        HttpMessageConverter<Resource> resource = new ResourceHttpMessageConverter();
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        formHttpMessageConverter.addPartConverter(jackson);
        formHttpMessageConverter.addPartConverter(resource);
        messageConverters.add(jackson);
        messageConverters.add(resource);
        messageConverters.add(formHttpMessageConverter);
        setMessageConverters(messageConverters);
        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, (chain, authType) -> true)
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, new NoopHostnameVerifier());
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                    new HttpComponentsClientHttpRequestFactory(httpclient);
            clientHttpRequestFactory.setConnectTimeout(TIMEOUT);
            setRequestFactory(clientHttpRequestFactory);
            setErrorHandler(new SCFResponseErrorHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Common non authorized request.
     *
     * @param resourceMapping request details.
     * @param request         request entity.
     * @param <R>             type of response.
     * @return response.
     */
    public <R> R executeRequest(ResourceMapping resourceMapping, CommonDTO request) {
        return executeRequest(null, resourceMapping, request);
    }

    /**
     * Common non authorized request.
     *
     * @param tokenDTO        token info.
     * @param resourceMapping request details.
     * @param request         request entity.
     * @param <R>             type of response.
     * @return response.
     */
    public <R> R executeRequest(TokenDTO tokenDTO, ResourceMapping resourceMapping, CommonDTO request) {
        String url = configuration.getServerUrl() + resourceMapping.getUrlSuffix();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(resourceMapping.getMediaType());
        if (tokenDTO != null && tokenDTO.getToken() != null) {
            httpHeaders.set(AUTH_HEADER_NAME, tokenDTO.getToken());
        }
        HttpEntity<CommonDTO> httpEntity = new HttpEntity<>(request, httpHeaders);
        RequestCallback requestCallback = httpEntityCallback(httpEntity, resourceMapping.getResponseClass().getType());
        HttpMessageConverterExtractor<R> responseExtractor =
                new HttpMessageConverterExtractor<R>(resourceMapping.getResponseClass().getType(), getMessageConverters());
        try {
            R response = execute(url, resourceMapping.getHttpMethod(), requestCallback, responseExtractor);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Multipart request executor.
     *
     * @param tokenDTO         token info.
     * @param resourceMapping  request details.
     * @param requstParameters request parameters.
     * @param <R>              type of response.
     * @return response.
     */
    public <R> R executeMultipartRequest(TokenDTO tokenDTO, ResourceMapping resourceMapping, LinkedMultiValueMap<String, Object> requstParameters) {
        String url = configuration.getServerUrl() + resourceMapping.getUrlSuffix();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(resourceMapping.getMediaType());
        if (tokenDTO != null && tokenDTO.getToken() != null) {
            httpHeaders.set(AUTH_HEADER_NAME, tokenDTO.getToken());
        }
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requstParameters, httpHeaders);
        try {
            ResponseEntity<R> response = exchange(url, resourceMapping.getHttpMethod(), requestEntity,
                    resourceMapping.getResponseClass());
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * File downloading.
     *
     * @param tokenDTO        token info.
     * @param resourceMapping request details.
     * @param outputFile      file which will be written.
     * @return downloaded file.
     */
    public File downloadFile(TokenDTO tokenDTO, ResourceMapping resourceMapping, File outputFile) {
        String url = configuration.getServerUrl() + resourceMapping.getUrlSuffix();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(resourceMapping.getMediaType());
        if (tokenDTO != null && tokenDTO.getToken() != null) {
            httpHeaders.set(AUTH_HEADER_NAME, tokenDTO.getToken());
        }
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, httpHeaders);
        try {
            ResponseEntity response = exchange(url, resourceMapping.getHttpMethod(), httpEntity, resourceMapping.getResponseClass());
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write((byte[]) response.getBody());
            fileOutputStream.flush();
            fileOutputStream.close();
            return outputFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
