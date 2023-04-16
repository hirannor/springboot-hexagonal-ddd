Spring-Boot - Ports-And-Adapters / Hexagonal Architecture with DDD

An example of a Spring-Boot application, which based on the port and adapters/hexagonal architecture and DDD.


In case of adapter swapping the application doesn't requires a re-build, adapters are configurable via application.yml.

```
...
adapter:
  authentication: basic
  persistence: spring-data-jpa
  messaging: spring-event-bus
  api: rest
....
```

