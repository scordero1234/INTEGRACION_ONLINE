package org.apache.camel.learn;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class Procesador implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String requestBody = exchange.getIn().getBody(String.class);
        System.err.println("Solicitud HTTP: " + requestBody);
}
}