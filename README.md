# Bitcoin

## Getting Started

### Prerequisites
- Git
- Maven 3.0+
- JDK 11+

### Clone

To get started you can simply clone this repository using git:
```
git clone https://github.com/devmanfredi/api-rest-bitcoin.git
cd api-rest-bitcoin
```

#### Develop

Run the application from the command line using:
```
mvn spring-boot:run
```

## API Documentation

http://localhost:8080/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

## Explore Rest APIs

Namespace     |   URL                        | HTTP Verb        | Result 
--------------|----------------------------- | ---------------- | -------------------------
Customers     | /api/v1/rest/customer        | POST             | Add Customer
Customers     | /api/v1/rest/customer        | GET              | Return all Customers
Customers     | /api/v1/rest/customer/:id    | GET              | Return customer by ID
Customers     | /api/v1/rest/customer/:id    | PUT              | transfers balance to the customer
Customers     | /api/v1/rest/customer/:id    | DELETE           | Remove customer
Customers     | /api/v1/rest/customer/bitcoin/:id     | POST    | Buy Bitcoin
              | Return customer authenticated

You can test them using postman or any other rest client.
