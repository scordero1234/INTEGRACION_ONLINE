# INTEGRACION EN LINEA
## Enunciado
Se necesita una solución en linea que permita pasar los clientes de la empresa XYZ a las empresas 123 y ABC en función del perfil del canal de compras del cliente, las empresas 123 y ABC utilizan sistemas de información o plataformas distintas, han logrado desarrollar un servicio web para recibir la información del cliente de la empresa XYZ, se han puesto de acuerdo para intercambiar la información en formato JSON. 

[Problematica a detalle](Problematica)

##AUTOR
```
SANTIAGO DAVID CORDERO CRESPO
Maestria en Software
 PATRONES DE INTEGRACION EMPRESARIA
Ing. Jorge Loja
```
### Codigo versionado

[https://github.com/scordero1234/INTEGRACION_ONLINE](https://github.com/scordero1234/INTEGRACION_ONLINE.git)

## Pasos para ejecutar
### Empresa 1 codigo python
Nos ubicamos dentra del  proyecto
```
cmd /pymicro
```
Ejecutamos 

```
 python main.py
```
Obtenemos:
![alt text](image.png)


### Empresa 2 codigo .Net
Nos ubicamos en la raiz del proyecto para ejecutar
```
cd /netmicro
```
ejecutamos

```
dotnet clean   
```
```
dotnet build
```
![alt text](image-1.png)

Finalmente arrancamos el sercicio

```
 dotnet run  
 ```
 ![alt text](image-2.png)
 
 ### CRM Empresa XYZ codigo SPRING

 Ingresamos al proyecto
 ```
 cd /spring-camel-integration
 ```
 ejecutamos 

 ```
 mvn clean package -DskipTest
 ```

 ![alt text](image-3.png)

 Levantamos el servicio
 
 ```
 java -jar target/spring-camel-integration-1.0.0-SNAPSHOT.jar
 ```
# PRUEBAS
 
### Empresa 1 codigo python
#### Creacion
Datos iniciales 
![alt text](image-5.png)
Se agrega 1 dato
![alt text](image-6.png)
#### Listado
Se lista el creado por defecto y el aregado por medio del servicio
![alt text](image-7.png)
### Empresa 2 codigo .Net
#### Creacion
Datos iniciales
![alt text](image-8.png)
Se agrega 1 dato
![alt text](image-9.png)
#### Listado
Se lista el creado por defecto y el aregado por medio del servicio
![alt text](image-10.png)



## Integracion

Para esta prueba debemos tener en cuenta segun el tipo de cliente, si el cliente es 1 usara los servicios de la empresa 1, servicio realizado en python.
Si el tipo de cliente es 2 consume los servicios de la empresa 2, realizado en .NET.

## Listado inicial
Se consume el servicio de la empresa 1 porque estoy enviado el tipo de cliente 1
![alt text](image-11.png)

Se consume el servicio de la empresa 2 porque estoy enviado el tipo de cliente 2

![alt text](image-12.png)
#### Creacion
Para la creacion  en la empresa 1, colocamos el tipo de cliente 1

![alt text](image-13.png)

Para la creacion  en la empresa 2, colocamos el tipo de cliente 2

![alt text](image-14.png)
#### Listado
Se consume el servicio de la empresa 1 porque estoy enviado el tipo de cliente 1

![alt text](image-15.png)
Se consume el servicio de la empresa 2 porque estoy enviado el tipo de cliente 2
![alt text](image-16.png)


#CODIGO DE ENRUTAMIENTO

```
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

```
