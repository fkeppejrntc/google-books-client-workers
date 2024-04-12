package com.nt.poc.mylibrary.workers;

import com.nt.poc.mylibrary.model.WppTemplateInput;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class CreateWppMessages {

    private final ZeebeClient zeebeClient;

    public CreateWppMessages(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "create-whatsapp-messages", autoComplete = true)
    public Map<String, Object> createWppMessages(final JobClient client,
                           final ActivatedJob job,
                           @Variable Integer timeOfLoan,
                           @Variable Double loanCost,
                           @Variable String bookTitle,
                           @Variable String bookLink) {
        log.info("Starting create-whatsapp-messages job worker");

        var today = new Date();

        var expireCalendar = Calendar.getInstance();
        expireCalendar.setTime(today);
        expireCalendar.add(Calendar.DATE, timeOfLoan);
        var expireDate = expireCalendar.getTime();
        log.info("Expiration Date: "+expireDate.toString());

        String costMessage = "";
        if (loanCost != null && loanCost > 0){
            costMessage = "Remember, this book loan has a cost of $ "+loanCost;
        } else {
            costMessage = "Remember, this book has no loan cost.";
        }

        WppTemplateInput inputItemBookTitle = new WppTemplateInput("text", bookTitle);
        WppTemplateInput inputItemBookLink = new WppTemplateInput("text", "URL to "+bookTitle);
        WppTemplateInput inputItemBookCost = new WppTemplateInput("text", costMessage);

        ArrayList<WppTemplateInput> loanMessageBody = new ArrayList<>();
        loanMessageBody.add(inputItemBookTitle);
        loanMessageBody.add(inputItemBookLink);
        loanMessageBody.add(inputItemBookCost);

        ArrayList<WppTemplateInput> notificationMessageBody = new ArrayList<>();
        notificationMessageBody.add(inputItemBookTitle);
        notificationMessageBody.add(inputItemBookCost);

        ArrayList<WppTemplateInput> returnMessageBody = new ArrayList<>();
        returnMessageBody.add(inputItemBookTitle);
        returnMessageBody.add(inputItemBookCost);

        String notificationMessage = "The e-Book '" + bookTitle +"' shall be returned in 2 days. "+costMessage;
        String returnMessage = "The e-Book '" + bookTitle +"' Is returning today. "+costMessage;

        Map<String, Object> variables = new HashMap<>();
        variables.put("loanMessageBody", loanMessageBody);
        variables.put("notificationMessageBody", notificationMessageBody);
        variables.put("returnMessageBody", returnMessageBody);
        variables.put("notificationMessage", notificationMessage);
        variables.put("returnMessage", returnMessage);

//        zeebeClient.newSetVariablesCommand(job.getProcessInstanceKey())
//                .variables(variables)
//                .send()
//                .join();

        return variables;

    }
}
