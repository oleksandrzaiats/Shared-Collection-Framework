package com.scf.client.resource;

import com.scf.client.config.Configuration;
import com.scf.shared.dto.CommonDTO;
import com.scf.shared.dto.TokenDTO;
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
        messageConverters.add(new MappingJackson2HttpMessageConverter());
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

    public <R> R executeRequest(ResourceMapping resourceMapping, CommonDTO request) {
        return executeRequest(null, resourceMapping, request);
    }

    public <R> R executeRequest(TokenDTO tokenDTO, ResourceMapping resourceMapping, CommonDTO request) {
        String url = configuration.getServerUrl() + resourceMapping.getUrlSuffix();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(resourceMapping.getMediaType());
        if(tokenDTO != null && tokenDTO.getToken() != null) {
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

}
