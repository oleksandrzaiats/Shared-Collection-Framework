package com.scf.client.resource;

import com.scf.client.config.Configuration;
import com.scf.shared.dto.CommonDTO;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.List;

public class SCFRestClient extends RestTemplate {

    private final Configuration configuration;
    // 30 seconds timeout
    private final int TIMEOUT = 30000;

    public SCFRestClient(Configuration configuration) {
        super();
        setRequestFactory(getClientHttpRequestFactory());
        List<HttpMessageConverter<?>> messageConverters = getMessageConverters();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        setMessageConverters(messageConverters);
        this.configuration = configuration;
    }

    private HttpClient createHttpClient() {
        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, (chain, authType) -> true)
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, new NoopHostnameVerifier());
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            return httpclient;
        }catch (Exception e) {

        }
        return null;
    }

    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(createHttpClient());
        clientHttpRequestFactory.setConnectTimeout(TIMEOUT);
        return clientHttpRequestFactory;
    }

    public <R> R executeRequest(ResourceMapping resourceMapping, CommonDTO request) {
        String url = configuration.getServerUrl() + resourceMapping.getUrlSuffix();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(resourceMapping.getMediaType());
        HttpEntity<CommonDTO> httpEntity = new HttpEntity<>(request, httpHeaders);
        RequestCallback requestCallback = httpEntityCallback(httpEntity, resourceMapping.getResponseClass());
        HttpMessageConverterExtractor<R> responseExtractor =
                new HttpMessageConverterExtractor<R>(resourceMapping.getResponseClass(), getMessageConverters());
        try {
            R response = execute(url, resourceMapping.getHttpMethod(), requestCallback, responseExtractor);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
