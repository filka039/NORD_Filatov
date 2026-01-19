package org.example.utils.wireMock;

public class WireMockConstants {
    public static final String LOGIN_ENDPOINT_MOCK = "/auth";
    public static final String ACTION_ENDPOINT_MOCK = "/doAction";
    public static final int PORT_MOCK = 8888;
    public static final int ACCESS_STATUS_MOCK = 200;
    public static final int BAD_REQUEST_STATUS_MOCK = 400;
    public static final String BODY_SUCCESS_MOCK = "{\"result\": \"OK\"}";
    public static final String BODY_ERROR_MOCK = "{\"result\": \"ERROR\", \n" +
            "\"message\": \"reason\" }";


}
