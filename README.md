# PurpleDog
- PurpleDog 사전 과제

## 프로젝트 개발 구성
- Java 8
- Spring Boot(+Maven) 2.6.7
- Spring Data JPA 2.6.7
- Spring Security 2.6.7
- JWT jwtt 0.9.1
- JUnit 5
- H2(Embedded DB) 1.4.200
- Swagger 3.0.0

## 프로젝트 서버 구성
- IP : localhost
- PORT : 8080

## H2(Embedded DB)
- Driver Class : org.h2.Driver
- JDBC URL : jdbc:h2:mem:testdb
- User Name : sa
- Password :
- Browser Connect URL Link
  - [http://localhost:8080/h2](http://localhost:8080/h2)

## Swagger
- Swagger Index URL Link
  - [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## API List
- **[POST createUser (JWT X)]**
  - localhost:8080/users
    - Request Body(json)
      - id
      - password
    - Response Body(json)
      - id
------------
- **[POST login (JWT X)]**
  - localhost:8080/login
    - Request Body(json)
      - id
      - password
    - Response Body(json)
      - id
      - **access_token**
------------
- **[PUT updateUser (JWT O)]**
  - localhost:8080/users
    - **Request Authorization(Bearer Token)**
      - access_token
    - Request Body(json)
      - password
    - Response Body(json)
      - id
------------
- **[DELETE deleteUser (JWT O)]**
  - localhost:8080/login
    - **Request Authorization(Bearer Token)**
      - access_token
    - Response Body(json)
      - id

## Schema 
- Users Query
  - create table users (  
      id varchar(50) not null,  
      created_date timestamp,  
      last_modified_date timestamp,  
      password varchar(250) not null,  
      primary key (id)  
    )