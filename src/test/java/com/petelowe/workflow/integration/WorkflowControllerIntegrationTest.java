package com.petelowe.workflow.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petelowe.workflow.domain.TaskId;
import com.petelowe.workflow.domain.Workflow;
import com.petelowe.workflow.domain.WorkflowId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.scheduling.config.Task;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkflowControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUri() {
        return "http://localhost:" + port;
    }

    @Test
    void createWorkflow_withValidRequest_shouldReturn200Response() throws JsonProcessingException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);

        final ResponseEntity<WorkflowId> workflow = restTemplate
                .exchange(getBaseUri() + "/api/v1/workflows/create",
                        HttpMethod.POST,
                        new HttpEntity<>(objectMapper
                                .writeValueAsString(
                                        new Workflow(
                                                "test",
                                                "test",
                                                Collections.emptyList())), headers),
                        WorkflowId.class);

        assertThat(workflow.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
