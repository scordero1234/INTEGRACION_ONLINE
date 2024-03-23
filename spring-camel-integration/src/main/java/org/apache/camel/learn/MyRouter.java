package org.apache.camel.learn;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonDataFormat;
import org.apache.camel.util.json.JsonArray;
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

    JacksonDataFormat jsonDataFormat = new JacksonDataFormat(PersonClient.class);
    @Override
    public void configure() throws Exception {
        from("direct:createPersonClient")
            .routeId("createPersonClient")
            .process(new Procesador())
            .marshal(jsonDataFormat) 

            .process(exchange -> { 
                if (camelContext != null) {
                    String requestBody = exchange.getIn().getBody(String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    PersonClient person = objectMapper.readValue(requestBody, PersonClient.class);
                    int clientType = person.getClientType();
                    exchange.setProperty("clientType", clientType);
                } else {
                    logger.warn("El contexto Camel no está disponible.");
                } 
            }).choice()
            
                .when(simple("${exchangeProperty.clientType} == 2")) // Creacion empresa 1 PYTHON
                    .to("rest:post:/clientes?host=localhost:5000")
                    .convertBodyTo(String.class)
                    .log("Se ha agregado el cliente en la empresa 1 PYTHON")
                    .to("rest:get:/clientes?host=127.0.0.1:5000") 
                    .convertBodyTo(String.class)
                .when(simple("${exchangeProperty.clientType} == 1")) // Creacion empresa 2 .NET
                .to("rest:post:/PersonClient?host=localhost:5075")
                   .to("rest:get:/PersonClient?host=localhost:5075")
                   .convertBodyTo(String.class)
                .log("Se ha agregado el cliente en la empresa 2 .NET")
                .otherwise()
                    .log("clientType no válido: ${exchangeProperty.clientType}") // Loguear un mensaje si clientType no es 1 ni 2
          
            .to("log:CREATE"); 

        from("direct:listarPersonClient")
            .routeId("listarPersonClient")
            .process(new Procesador())
            .marshal(jsonDataFormat) 
            .process(exchange -> { 
                if (camelContext != null) {
                    String requestBody = exchange.getIn().getBody(String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    PersonClient person = objectMapper.readValue(requestBody, PersonClient.class);
                    int clientType = person.getClientType();
                    exchange.setProperty("clientType", clientType);
                    logger.info("Solicitud Peticion HTTP: {}", requestBody);
                } else {
                    logger.warn("El contexto Camel no está disponible.");
                }
                }).choice()
                .when(simple("${header.clientType} == 2")) // clientType es igual a 2 PYTHON Empresa 1
                    .to("rest:get:/clientes?host=127.0.0.1:5000") 
                    .convertBodyTo(String.class)
                    .log("Ha recibido la respuesta de la empresa 1 PYTHON")
                .when(simple("${header.clientType} == 1")) //  clientType es igual a 1 .NET Empresa 2
                    .to("rest:get:/PersonClient?host=localhost:5075")
                    .convertBodyTo(String.class)
                    .log("Ha recibido la respuesta de la empresa 2 .NET")
                .otherwise()
                    .log("clientType no válido: ${header.clientType}"); // Loguear un mensaje si clientType no es 1 ni 2
              
    
    }
}
