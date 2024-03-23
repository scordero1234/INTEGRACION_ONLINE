package org.apache.camel.learn;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuyService {
    @Autowired private ProducerTemplate producerTemplate;
    
    @GetMapping("/listarPersonClient")
    public String buy(@RequestBody PersonClient personClient) {
        String result = producerTemplate.requestBody("direct:listarPersonClient", personClient).toString();
        return result;
    }

    @PostMapping("/createPersonClient")
    public String createPersonClient(@RequestBody PersonClient personClient) {
        String result = producerTemplate.requestBody("direct:createPersonClient", personClient).toString();
        return result;
    }
    
}
