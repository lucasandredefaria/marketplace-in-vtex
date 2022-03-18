# Spring Boot for sending product updates and inserting new products from the marketplace on VTEX
This application receives messages from RabbitMQ queues and parses the objects to send them to VTEX.

## Installation
	mvn clean install
	mvn spring-boot:run

## Use
The queues that the application listens for are:

- produto
  * Receives an object to update/register product and sku on VTEX.
  
- estoque
  * Receives an object to update product stock on VTEX.
  
- preco
  * Receives an object to update product price on VTEX.

## Application Health Check
This method returns application health check

	curl -v -H "Content-Type: application/json" http://localhost:8080/actuator/health

