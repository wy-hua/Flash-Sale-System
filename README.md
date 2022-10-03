# Flash Sale System

## Feature

In a flash-sale scenario, numerous client requests will rush into the server, possibly leading to a server crash. To improve the performance, in-memory cache and message queues are introduced in this project.
Cache(Redis)
Product information is stored in the Redis docker container to speed up writes and reads.
Message Queue(Rabbitmq)
Every purchase request doesn't go directly to the server. Instead, the request is sent to an intermediate RabbitMq server. The message queue server will asynchronously send the message to the consumption function. In this way,

## To Do List

- [x] code the data access object and service layers

- [x] add message queue service to the project 

- [x] finish the controller layer

  
