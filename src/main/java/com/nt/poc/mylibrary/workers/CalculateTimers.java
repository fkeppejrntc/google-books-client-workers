package com.nt.poc.mylibrary.workers;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CalculateTimers {

    private final ZeebeClient zeebeClient;

    public CalculateTimers(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "calculate-timers", autoComplete = true)
    public void calculateTimers(final JobClient client,
                           final ActivatedJob job,
                           @Variable Integer timeOfLoan) {
        log.info("Starting calculate-timers job worker");

        var today = new Date();

        var notifCalendar = Calendar.getInstance();
        notifCalendar.setTime(today);
        //notifCalendar.add(Calendar.DATE, timeOfLoan-2);
        notifCalendar.add(Calendar.MINUTE, 1);
        var notifDate = notifCalendar.getTime();
        log.info("Notification Date: "+notifDate.toString());

        var expireCalendar = Calendar.getInstance();
        expireCalendar.setTime(today);
        //expireCalendar.add(Calendar.DATE, timeOfLoan);
        expireCalendar.add(Calendar.MINUTE, 2);
        var expireDate = expireCalendar.getTime();
        log.info("Expiration Date: "+expireDate.toString());

        Map<String, Object> variables = new HashMap<>();
        variables.put("notificationDate", notifDate);
        variables.put("expirationDate", expireDate);

        zeebeClient.newSetVariablesCommand(job.getProcessInstanceKey())
                .variables(variables)
                .send();

    }
}
