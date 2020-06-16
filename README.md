# Reboardify
## Accenture - Java Competition 
Reaboardify is an application built for the Accenture Java competition.

The task was to build an application in microservice architecture for the safe reboarding of the employees after the COVID-19 pandemic.
It is possible to set the percentage of employees that can stay in office at the same time.

## Features
**/register** - receives an Employee object with an "id" field and registers that employee. If the registered employee exceeds the number of the people that is already in the building he/she gets on a queue list.\
**/status** - receives an Employee object with an "id" field and returns the position for that employee.\
**/entry** - receives an Employee object with an "id" field and it returns with a message if the employee was authorized to enter or not.\
**/exit** - receives an Employee object with an "id" field and it returns with a message if the employee was authorized to exit or tried to leave without entering the office first.\

(The register endpoint resets the authorized and queue lists if its a new day.)

## Local Environment Variables

### Database microservice

**PORT** = any unused port but it will be used in the DB_MS_URL for other microservices\
**CAPACITY** = percentage of total employees that can enter (250) (Postman script and Unit/API tests are requiring CAPACITY to set to 1)

### Service discovery

**PORT** = any unused port but it will be used in the EUREKA_URL for other microservices \
**EUREKA_URL** = http://<span></span>localhost:(service discovery port)/eureka/

### Register, Status, Entry, Exit microservices

**PORT** = individual unsued port for each services\
**DB_MS_URL** = http://<span></span>localhost:(database microservice port)\
**EUREKA_URL** = http://<span></span>localhost:(service discovery port)/eureka/

### Application

**PORT** is set to static 9999 due to postman testing\
**EUREKA_URL** = http://<span></span>localhost:(service discovery port)/eureka/