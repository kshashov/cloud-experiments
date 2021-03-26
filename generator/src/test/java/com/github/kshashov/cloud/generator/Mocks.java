package com.github.kshashov.cloud.generator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class Mocks {

    public static void producerAdd(WireMockServer mockService, String body) {
        mockService.stubFor(WireMock.put(WireMock.urlEqualTo("/tasks"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.CREATED.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)));
    }
}
