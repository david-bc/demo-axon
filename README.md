# Axon 3 Demo

Docs: https://docs.axonframework.org/v/3.0/

## Dependencies

Spring: https://mvnrepository.com/artifact/org.axonframework/axon-spring
Test: https://mvnrepository.com/artifact/org.axonframework/axon-test

```
compile group: 'org.axonframework', name: 'axon-spring', version: '3.0.3'
testCompile group: 'org.axonframework', name: 'axon-test', version: '3.0.3'
```

## Video 1

Introduction to Axon and basics of:

- Commands: Handler calculates state change
- Events: Handler applies state change
- Aggregates: Persist state change (db entity)


https://www.youtube.com/watch?v=s2zH7BsqtAk

## Video 2

Axon Sagas

- Sagas to mange transactions
- Binding events to existing sagas

https://www.youtube.com/watch?v=Fj365BufWNU

## Video 3

Query Collections

- Using Events(/Sagas) to update managed aggregate collections
- Exposing those collections to a REST API
- Setting up everything in Spring Boot

https://www.youtube.com/watch?v=qqk2Df_0Pm8

Take a look at `$BASE/Axon Tutorial.postman_collection`