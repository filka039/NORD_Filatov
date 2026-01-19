package org.example.requests.httpCore;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.example.constants.Endpoints;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Log
@AllArgsConstructor
@NoArgsConstructor
public class HttpCore {

    public HttpCore(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Setter
    private Map<String, String> headers = new HashMap<>();

    @Setter
    private String baseUrl = Endpoints.BASE_URL;

    private RequestSpecBuilder baseBuilder() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.URLENC);

        if (headers != null && !headers.isEmpty()) {
            builder.addHeaders(headers);
        }

        return builder;
    }

    private RequestSpecification buildRequestSpecification(Map<String, String> customHeaders, Object body) {
        RequestSpecBuilder builder = baseBuilder();
        if (customHeaders != null && !customHeaders.isEmpty()) {
            builder.addHeaders(customHeaders);
        }
        builder.setBody(body);
        return given(builder
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build());
    }

    private RequestSpecification buildRequestSpecification(Map<String, String> customHeaders) {
        RequestSpecBuilder builder = baseBuilder();
        if (customHeaders != null && !customHeaders.isEmpty()) {
            builder.addHeaders(customHeaders);
        }
        return given(builder
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build());
    }

    public Response post(String path, Map<String, String> headers, Object body) {
        return buildRequestSpecification(headers, body).when().post(path);
    }

    public Response post(String path, Object body) {
        return buildRequestSpecification(headers, body).when().post(path);
    }

    public Response post(String path, Map<String, String> headers) {
        return buildRequestSpecification(headers).when().post(path);
    }
}
