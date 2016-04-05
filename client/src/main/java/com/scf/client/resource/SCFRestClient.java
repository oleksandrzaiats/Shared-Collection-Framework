package com.scf.client.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.client.config.Configuration;
import com.scf.client.exception.SCFServerException;
import com.scf.shared.dto.CommonDTO;
import com.scf.shared.dto.ErrorDTO;
import com.scf.shared.dto.TokenDTO;
import okhttp3.*;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.*;
import javax.ws.rs.HttpMethod;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.scf.shared.utils.StringUtils.AUTH_HEADER_NAME;

public class SCFRestClient {

    private final Configuration configuration;
    // 30 seconds timeout
    private final int TIMEOUT = 30;

    OkHttpClient client;
    ObjectMapper mapper = new ObjectMapper();

    public SCFRestClient(Configuration configuration) {
        super();
        try {
            client = getUnsafeOkHttpClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.configuration = configuration;
    }

    private OkHttpClient getUnsafeOkHttpClient() throws Exception {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);

        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }

    /**
     * Common non authorized request.
     *
     * @param resourceMapping request details.
     * @param request         request entity.
     * @param <Res>           type of response.
     * @return response.
     */
    public <Req extends CommonDTO, Res> Res executeRequest(ResourceMapping resourceMapping, Req request) {
        return executeRequest(null, resourceMapping, request);
    }

    /**
     * Common non authorized request.
     *
     * @param tokenDTO        token info.
     * @param resourceMapping request details.
     * @param request         request entity.
     * @param <Res>           type of response.
     * @return response.
     */
    public <Req extends CommonDTO, Res> Res executeRequest(TokenDTO tokenDTO, ResourceMapping resourceMapping, Req request) {
        String url = configuration.getServerUrl() + resourceMapping.getUrlSuffix();
        Res response = exchange(url, tokenDTO, request, resourceMapping);
        return response;
    }

    /**
     * Multipart request executor.
     *
     * @param tokenDTO          token info.
     * @param resourceMapping   request details.
     * @param requestParameters request parameters.
     * @param <Res>             type of response.
     * @return response.
     */
    public <Res> Res executeMultipartRequest(TokenDTO tokenDTO, ResourceMapping resourceMapping, Map<String, Object> requestParameters) {
        String url = configuration.getServerUrl() + resourceMapping.getUrlSuffix();
        try {
            Res response = exchangeMultipart(url, tokenDTO, requestParameters, resourceMapping);
            return response;
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
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (tokenDTO != null && tokenDTO.getToken() != null) {
            requestBuilder.addHeader(AUTH_HEADER_NAME, tokenDTO.getToken());
        }
        requestBuilder.addHeader("Content-Type", resourceMapping.getMediaType());
        requestBuilder.method(resourceMapping.getHttpMethod(), null);
        Response rawResponse = makeCall(requestBuilder);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(rawResponse.body().bytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return outputFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <Req extends CommonDTO, Res> Res exchange(String url, TokenDTO tokenDTO, Req request, ResourceMapping resourceMapping) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (tokenDTO != null && tokenDTO.getToken() != null) {
            requestBuilder.addHeader(AUTH_HEADER_NAME, tokenDTO.getToken());
        }
        requestBuilder.addHeader("Content-Type", resourceMapping.getMediaType());
        try {
            requestBuilder.method(resourceMapping.getHttpMethod(), resourceMapping.getHttpMethod().equals(HttpMethod.GET) ? null : RequestBody.create(MediaType.parse(resourceMapping.getMediaType()), mapper.writeValueAsString(request)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response rawResponse = makeCall(requestBuilder);
        try {
            byte[] bytes = rawResponse.body().bytes();
            if (bytes.length > 0) {
                Res response = (Res) mapper.readValue(bytes, resourceMapping.getResponseClass());
                return response;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private <Res> Res exchangeMultipart(String url, TokenDTO tokenDTO, Map<String, Object> request, ResourceMapping resourceMapping) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, Object> entry : request.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof File) {
                File fileValue = (File) value;
                multipartBuilder.addFormDataPart(entry.getKey(), fileValue.getName(),
                        RequestBody.create(MediaType.parse(new MimetypesFileTypeMap().getContentType(fileValue)), fileValue));

            } else {
                multipartBuilder.addFormDataPart(entry.getKey(), value.toString());
            }
        }
        RequestBody requestBody = multipartBuilder.build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (tokenDTO != null && tokenDTO.getToken() != null) {
            requestBuilder.addHeader(AUTH_HEADER_NAME, tokenDTO.getToken());
        }
        requestBuilder.method(resourceMapping.getHttpMethod(), requestBody);
        Response rawResponse = makeCall(requestBuilder);
        Res response = null;
        try {
            response = (Res) mapper.readValue(rawResponse.body().bytes(), resourceMapping.getResponseClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private Response makeCall(Request.Builder requestBuilder) {
        try {
            Response response = client.newCall(requestBuilder.build()).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                try {
                    ErrorDTO errorDTO = mapper.readValue(response.body().bytes(), ErrorDTO.class);
                    throw new SCFServerException(response.code(), errorDTO);
                } catch (IOException e) {
                    throw new SCFServerException(response.code(), response.message());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
