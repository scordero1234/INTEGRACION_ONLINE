package org.apache.camel.learn;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuyService {
    @Autowired private ProducerTemplate producerTemplate;
    
    @PostMapping("/buy")
    public String buy(@RequestBody Person person) {
        String result = producerTemplate.requestBody("direct:processPurchase", person).toString();
        return result;
    }

    @PostMapping("/createPerson")
    public String createPerson(@RequestBody Person person) {
        String result = producerTemplate.requestBody("direct:createPerson", person).toString();
        return result;
    }
    
}
