# openpd

Application for transferring money between accounts

Technology Stack
Java 8 Apache Spark JUnit Maven

Steps to run the application
Executable JAR file is in the location account/target/account-0.0.1-SNAPSHOT.jar Download the JAR file to local file system. Ensure Java 8 is installed and JRE is available in the path. Type the below command to confirm Java installation cmd> java -version

Run the below command to bring up the application. cmd> java -jar account-0.0.1-SNAPSHOT.jar

REST Endpoints
Invoke the below end points from Postman to execute the REST APIs. Sample payload details provided below.

POST http://localhost:8085/account/v1/create

DELETE http://localhost:8085/account/v1/delete/16330131

PATCH http://localhost:8085/account/v1/transfer http://localhost:8085/account/v1/credit http://localhost:8085/account/v1/debit

GET http://localhost:8085/account/v1/balance/:id http://localhost:8085/account/v1/account/:id http://localhost:8085/account/v1/accounts

Sample Payload
For creating new accounts
{ "name": "John", "currency": "GBP", "balance": 1200 }

{ "name": "Steve", "currency": "GBP", "balance": 45500 }

For credit/debit/transfer
{ "fromAccountId": 1234567, "toAccountId": 5656565656, "amount": 1200 }
