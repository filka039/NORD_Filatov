package org.example.utils.wireMock;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class WireMockUtil {
    private static WireMockServer wireMockServer;

    private WireMockUtil() {};

    public static WireMockServer getInstance(int port) {
        if (wireMockServer == null) {
            synchronized (WireMockUtil.class) {
                if (wireMockServer == null) {
                    wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(port));
                    wireMockServer.start();
                    WireMock.configureFor("localhost", wireMockServer.port());
                }
            }
        }
        return wireMockServer;
    }

    public static void stopMockServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    public static int getPort() {
        return (wireMockServer != null && wireMockServer.isRunning()) ? wireMockServer.port() : -1;
    }

    public static void resetAll() {
        WireMock.reset();
    }

    public static boolean isServerRunning() {
        return wireMockServer != null && wireMockServer.isRunning();
    }

    public static void stubPost(String endpoint, int status, String response) {
        stubFor(post(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withBody(String.valueOf(response))));
    }
}
