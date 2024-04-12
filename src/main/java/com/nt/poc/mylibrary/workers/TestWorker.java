package com.nt.poc.mylibrary.workers;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestWorker {

    private static final String DEBUG_MESSAGE = "test worker debugging {}";

    @JobWorker(type = "mylibrarytestworker", autoComplete = true)
    public void testWorker(final JobClient client, final ActivatedJob job) {
        log.info("Starting test worker");
    }
}
