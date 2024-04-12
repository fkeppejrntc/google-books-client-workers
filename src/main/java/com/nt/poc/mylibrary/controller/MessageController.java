package com.nt.poc.mylibrary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nt.poc.mylibrary.model.ProcessInstanceMessageDto;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/zeebe")
@Validated
public class MessageController {

    private final ZeebeClient zeebeClient;
    private final HttpServletRequest request;

    public MessageController(ZeebeClient zeebeClient, HttpServletRequest request) {
        this.zeebeClient = zeebeClient;
        this.request = request;
    }

    @PostMapping(
            path = "/send-message",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<ProcessInstanceMessageDto> sendMessage(
            @RequestBody String body) throws JsonProcessingException {

        JSONObject jsonBody = new JSONObject(body);
        String messageName = jsonBody.get("messageName").toString();
        String correlationKey = jsonBody.get("correlationKey").toString();
        String variables = jsonBody.get("variables").toString();

        log.info("SENDING MESSAGE " + messageName + " WITH CORRELATION KEY "+correlationKey);



        final PublishMessageResponse event =
                zeebeClient
                        .newPublishMessageCommand()
                        .messageName(messageName)
                        .correlationKey(correlationKey)
                        .variables(variables)
                        .send()
                        .join();

        ProcessInstanceMessageDto resp =
                ProcessInstanceMessageDto.builder().key(String.valueOf(event.getMessageKey())).build();

        var response =
                ResponseEntity.ok(resp);

        log.info("MESSAGE " + messageName + " WITH CORRELATION KEY "+correlationKey+" SENT");

        return response;
    }

}
