# Spring-Boot - Ports-And-Adapters / Hexagonal Architecture with DDD
![Build status](https://img.shields.io/github/actions/workflow/status/hirannor/springboot-hexagonal-ddd/.github/workflows/maven.yml) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Overview
An example of a Spring-Boot application, which based on the port and adapters/hexagonal architecture and DDD.

## Key concept

The key concept is that the component scan for the adapter package is excluded in the application, so the unnecessary adapter beans won't be loaded into the application context, just the configured ones.
Each adapter should define her own spring configuration class, which is getting only component scanned if the condition fulfills (via @ConditionalOnProperty) for it.


### Example

```java
@Configuration
@EnableJpaRepositories
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.persistence",
        havingValue = "spring-data-jpa"
)
public class JpaPersistenceConfiguration {

}
```

application-[profile].yml
```YAML
adapter:
  authentication: basic
  persistence: spring-data-jpa
  messaging: spring-event-bus
  web: rest
```

## Prerequisites for development

- [Git](https://git-scm.com/downloads)
- [JDK 17](https://adoptium.net/)
- [Maven](https://maven.apache.org/download.cgi)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)



