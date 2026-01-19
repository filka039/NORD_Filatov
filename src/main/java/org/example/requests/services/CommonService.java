package org.example.requests.services;

import io.restassured.response.Response;
import org.example.constants.Endpoints;
import org.example.requests.httpCore.HttpCore;

import java.util.Map;

public class CommonService extends HttpCore {

    public Response postRequest(Map<String, String> headers, String body) {
        return post(Endpoints.COMMON_ENDPOINT, headers, body);
    }

    public Response postRequest(String body) {
        return post(Endpoints.COMMON_ENDPOINT, body);
    }

    public Response postRequest(Map<String, String> headers) {
        return post(Endpoints.COMMON_ENDPOINT, headers);
    }
}
