package com.example.wiremockissue;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.util.StreamUtils.copyToByteArray;

public class MultipartStubTest {

    public static final String TEST_FILE = "simple.pdf";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void should_deliver_stub_for_multipart_request() throws Exception {

        // GIVEN
        givenStubForMultipartRequest();

        // WHEN
        InputStream stream = new ClassPathResource(TEST_FILE).getInputStream();
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        stream.close();
        HttpResponse<String> response = Unirest.post(wireMockRule.baseUrl())
                .field("name", "John Doe")
                .field("file", bytes, "somefile.txt")
                .asString();

        // THEN
        String msg = response.getBody();
        assertFalse(msg, response.getBody().contains(
                        "java.lang.IllegalStateException: No multipart config for servlet"
                ));
        assertEquals(200, response.getStatus());
    }

    private void givenStubForMultipartRequest() throws Exception {
        try (InputStream pdf = new ClassPathResource(TEST_FILE).getInputStream()) {
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
