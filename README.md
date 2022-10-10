# Flash Sale System

## Feature

In a flash-sale scenario, numerous client requests will rush into the server, possibly leading to a server crash. To improve the performance, in-memory cache and message queues are introduced in this project.

Cache(Redis)
Product information is stored in the Redis docker container to speed up writes and reads. Besides, with the help of the  ,stocks of products will be decreased before the data is written to database.

Message Queue(RabbitMQ)
Every purchase request doesn't go directly to the server. Instead, the request is sent to an intermediate RabbitMQ server. The message queue server will asynchronously send the message to the consumption function. In this way,

## To Do List

- [x] code the data access object and service layers
- [x] add message queue service to the project 
- [x] finish the controller layer
- [ ] add authentication and authorization 



## How TO Solve Potential Problems

security of APIs
The purchase HTTP request URL

overselling
In one-machine deployment, Redis atomic decrement assure the correctness of decreasing the stock. When the amount of product is found to be negative after the decrement, a global exception is thrown and will be caught so that users will be informed that the product is out of stock.

low API response time

rate limit

global exception

Message queues lost messages 
