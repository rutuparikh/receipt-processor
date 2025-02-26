# Receipt Processor

Receipt Processor is a webservice that accepts receipts of retail shopping and generates points for each of them. It fulfills the API definition provided at [api.yml](https://github.com/rutuparikh/receipt-processor/blob/main/api.yml).

## Technology stack & development tools
- Java (Spring Boot)
- Redis
- Docker
- Maven build tool
- JUnit test framework

## How to run the application ?
This is a dockerized setup, to run the service, clone and execute below command at the root of the directory.
```
docker compose up
```
The service is published at HTTP port 8080. 

## Following APIs are exposed:
- process receipt
    - Path: /receipts/process
    - Method: POST
    - Payload: Receipt JSON
    - Response: JSON containing an id for the receipt.
    - Description: Takes in a JSON receipt and returns a JSON object with an ID generated.
- get points
    - Path: /receipts/{id}/points
    - Method: GET
    - Response: A JSON object containing the number of points awarded.
    - Description: A Getter endpoint that looks up the receipt by the ID and returns an object specifying the points awarded.
 
## Future enhancements
1. Cloud Architecture
   ![image](https://github.com/user-attachments/assets/416ab6ac-4867-492d-be75-07993f8d8d1d)

2. Security
    - HTTPS encryption
    - Rate limiting
    - Authenticate users using OAuth
