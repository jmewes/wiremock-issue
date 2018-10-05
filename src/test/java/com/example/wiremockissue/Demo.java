package com.example.wiremockissue;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.util.StreamUtils.copyToByteArray;

public class Demo {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void demo() throws Exception {

        // GIVEN
        givenSuccessResponseBody();

        // WHEN
        final InputStream stream = new ClassPathResource("simple.pdf").getInputStream();
        final byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        stream.close();
        final HttpResponse<String> jsonResponse = Unirest.post(wireMockRule.baseUrl())
                .field("name", "Mark")
                .field("file", bytes, "image.jpg")
                .asString();

        // THEN
        assertTrue(jsonResponse.getBody().contains("java.lang.IllegalStateException: No multipart config for servlet"));
        assertEquals(200, jsonResponse.getStatus());
    }

    private void givenSuccessResponseBody() throws Exception {
        try (InputStream pdf = new ClassPathResource("simple.pdf").getInputStream()) {
            byte[] body = copyToByteArray(pdf);
            stubFor(post(urlPathMatching(".*"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", APPLICATION_PDF_VALUE)
                            .withHeader("Content-Length", String.valueOf(body.length))
                            .withBody(body)
                    ));
        }
    }
}
