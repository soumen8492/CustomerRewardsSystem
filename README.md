
# Customer Rewards System Project

This is a project that calculates and displays rewards points earned by customers of a retail company

## Installation

This project is a maven project. Codes written in Java 11 and Spring boot 3.0.0+
* Clone this repository ```git clone https://github.com/soumen8492/CustomerRewardsSystem.git```
* Make sure you are using JDK 1.11 and Maven 3.x

## Run the project
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by using the command in CLI:
```
mvn spring-boot:run
```
* Check the stdout to make sure no exceptions are thrown

Once the application runs you should see something like this

```
2025-01-28T15:48:18.958+05:30  INFO 13136 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-01-28T15:48:18.972+05:30  INFO 13136 --- [           main] C.CustomerRewardsSystemApplication       : Started CustomerRewardsSystemApplication in 3.577 seconds (process running for 4.64)
```

## About the Service

The service is just a simple reward points calculator service. its has a 3 months worth of data of customer transactions. It calculates the points based on amount spent on an item. The app uses in-memory database.
 
This little application demonstrates: 

* Full fledged Java Spring boot application
* A RESTful service to calculate and display reward points for each customer
* Exception mapping from application exceptions to the right HTTP response with exception details in the body
* *H2 Database* Integration with JPA with just a few lines of configuration and familiar annotations. 
* Demonstrates MockMVC test framework with associated libraries

Here are some endpoints you can call:

## Endpoints
1. Get All Transactions
```
URL: /allTransaction
Method: GET
Description: Retrieves a list of all transactions.
Response: ResponseEntity<List<Transaction>>
Example:
json
[
  {
    "tranId": "1",
    "custId": "1",
    "amount": 150,
    "mnth": "jan",
    "points": 0
  },
  ...
]
```
2. Calculate Reward Points
```
URL: /calcRewards
Method: PATCH
Description: Calculates and updates reward points for all transactions.
Response: ResponseEntity<List<Transaction>>
Example:
json
[
  {
    "tranId": "1",
    "custId": "1",
    "amount": 150,
    "mnth": "jan",
    "points": 10
  },
  ...
]
```
3. Get Transactions by Customer ID and Month
```
URL: /transactions/{customerId}/{month}
Method: GET
Description: Retrieves a list of transactions for a specific customer and month.
Path Variables:
customerId (String): The ID of the customer.
month (String): The month of the transactions.
Response: ResponseEntity<List<Transaction>>
Example:
json
[
  {
    "tranId": "1",
    "custId": "1",
    "amount": 170,
    "mnth": "jan",
    "points": 190
  },
  ...
]
```
4. Get Transactions by Customer ID
```
URL: /transactions/{customerId}
Method: GET
Description: Retrieves a list of transactions for a specific customer.
Path Variables:
customerId (String): The ID of the customer.
Response: ResponseEntity<List<Transaction>>
Example:
json
[
  {
    "tranId": "1",
    "custId": "1",
    "amount": 150,
    "mnth": "jan",
    "points": 0
  },
  ...
]
```
5. Get Total Rewards
```
URL: /total
Method: GET
Description: Retrieves the total rewards for all customers with all months.
Response: List<Reward>
Example:
json
[{
  "custId": "1",
  "monthlyRewards": {
    "feb": 3668,
    "jan": 3302,
    "mar": 5028
  },
  "totalRewards": 16998
}.....
]

```
6. Get Total Rewards by Customer ID
```
URL: /total/{customerId}
Method: GET
Description: Retrieves the total rewards for a specific customer.
Path Variables:
customerId (String): The ID of the customer.
Response: ResponseEntity<Reward>
Example:
json
{
  "custId": "1",
  "monthlyRewards": {
    "feb": 3668,
    "jan": 3302,
    "mar": 5028
  },
  "totalRewards": 16998
}
```

### To view your H2 in-memory datbase

The application runs on H2 in-memory database. To view and query the database you can browse to http://localhost:8080/h2-console. Default username is 'sa' with a blank password.

# Questions and Comments: soumen8492@gmail.com
