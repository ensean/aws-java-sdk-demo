package org.example;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.AddressConfiguration;
import software.amazon.awssdk.services.pinpoint.model.MessageRequest;
import software.amazon.awssdk.services.pinpoint.model.SendMessagesRequest;
import software.amazon.awssdk.services.pinpoint.model.Template;
import software.amazon.awssdk.services.pinpoint.model.TemplateConfiguration;

public class PinpointDemo {
    public static void main(String[] args) {

        // setup pinpoint client
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .build();


        // set addresses
        Map<String, AddressConfiguration> toAddress = new HashMap<String, AddressConfiguration>();
        // channel type set to EMAIL
        AddressConfiguration aConfiguration = AddressConfiguration.builder()
                .channelType("EMAIL")
                .build();
        toAddress.put("aaaa@gmail.com", aConfiguration);

        // set template
        Template emailTemplate = Template.builder()
                .name("xxxx")
                .version("1")
                .build();
        // set template configuration
        TemplateConfiguration templateConfiguration = TemplateConfiguration.builder()
                .emailTemplate(emailTemplate)
                .build();
        // set MessageRequest
        MessageRequest mRequest = MessageRequest.builder()
                .addresses(toAddress)
                .templateConfiguration(templateConfiguration)
                .build();
        //set sendmessage request
        SendMessagesRequest sendMessagesRequest = SendMessagesRequest.builder()
                .applicationId("xx")
                .messageRequest(mRequest)
                .build();
        // send message
        try {
            pinpoint.sendMessages(sendMessagesRequest);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
