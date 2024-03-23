package org.apache.camel.learn;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MyRouter extends RouteBuilder {
  @Autowired
    private CamelContext camelContext;

    private Logger logger = LoggerFactory.getLogger(MyRouter.class);

    JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Person.class);
    @Override
    public void configure() throws Exception {
        from("direct:createPerson")
            .routeId("createPerson")
            .process(new Procesador())
            .marshal(jsonDataFormat) 

            .process(exchange -> { 
                if (camelContext != null) {
                    String requestBody = exchange.getIn().getBody(String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Person person = objectMapper.readValue(requestBody, Person.class);
                    int clientType = person.getClientType();
                    exchange.setProperty("clientType", clientType);
                    logger.info("Solicitud HTTP: {}", requestBody);
                } else {
                    logger.warn("El contexto Camel no está disponible.");
                } 
            })
           
    }
}
