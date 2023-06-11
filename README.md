Spring-Boot - Ports-And-Adapters / Hexagonal Architecture with DDD

An example of a Spring-Boot application, which based on the port and adapters/hexagonal architecture and DDD.

## Prerequisites for development

The following tools should be installed:

- [Git](https://git-scm.com/downloads)
- [JDK 17](https://adoptium.net/)
- [Maven](https://maven.apache.org/download.cgi)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)


**Configuration:**

```
adapter:
  authentication: basic
  persistence: spring-data-jpa
  messaging: spring-event-bus
  web: rest
```

