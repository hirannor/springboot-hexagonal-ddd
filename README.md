# Spring-Boot - Ports-And-Adapters / Hexagonal Architecture with DDD

|Build Status|License|
|------------|-------|
|[![Build Status](https://img.shields.io/github/actions/workflow/status/hirannor/springboot-hexagonal-ddd/.github/workflows/maven.yml)](https://github.com/hirannor/springboot-hexagonal-ddd/actions/workflows/maven.yml)|[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)|


## Overview

This project is a **Spring Boot** example implementing **Ports & Adapters / Hexagonal Architecture** with **Domain-Driven Design (DDD)** principles.

The architecture enforces separation of concerns:

* **Domain Layer:** Pure business logic, framework-agnostic.
* **Application Layer:** Orchestrates use cases and mediates between domain and adapters.
* **Adapters / Infrastructure Layer:** Implements technical concerns such as messaging, persistence, and external APIs.

Architectural rules are validated using **[ArchUnit](https://www.archunit.org/)** to maintain a clean and consistent project structure.

For DDD modeling, the project uses an **Order Management System** as the core domain, since it naturally covers a wide variety of concepts:

* **Customer** – aggregate root representing a registered user of the system.  
  Customers can register, authenticate, and manage their own profile.  
  Admins use the same `Customer` API but with extended permissions (e.g., list all, delete).
* **Order** – aggregate root handling the full order lifecycle: creation, status changes, payment, cancellation, shipping, etc.
* **Basket** – aggregate root representing a shopping basket where products can be added/removed before checkout.
* **Product** – aggregate root representing catalog items that can be queried and added to baskets or orders.

Other concerns such as authentication and authorization are modeled separately via `AuthUser` (a value object), rather than as a dedicated aggregate root. Roles (`CUSTOMER`, `ADMIN`) control access to APIs instead of introducing extra domain entities.


## 🛠 Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat\&logo=java\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat\&logo=spring-boot\&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat\&logo=git\&logoColor=white)
![SQL](https://img.shields.io/badge/SQL-003B57?style=flat\&logo=microsoft-sql-server\&logoColor=white)


## Architecture Overview

![Architecture](img/architecture.svg)

Key principles enforced via ArchUnit tests:

* Dependency rules between layers (Domain → Application → Adapters).
* Isolation of Domain Layer from frameworks.
* Compliance with naming conventions and package structure.

Running these tests ensures **architectural integrity** as the project evolves.


## Getting Started

### Prerequisites

* [Git](https://git-scm.com/downloads)
* [JDK 21](https://adoptium.net/)
* [Maven](https://maven.apache.org/download.cgi)
* [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Quick Start

1. Clone the repository:

```bash
git clone https://github.com/hirannor/springboot-hexagonal-ddd.git
cd springboot-hexagonal-ddd
```

2. Start the PostgreSQL database with Docker:

```bash
docker-compose up -d
```

3. Build and run tests:

```bash
mvn clean verify
```

***Note:*** Some tests use **Testcontainers**, so Docker must be running.


## Implementing a New Adapter

Adapters are **explicitly configured**—the application excludes scanning the entire adapter package to avoid loading unnecessary beans.

* Each adapter defines its own Spring configuration class.
* Classes are imported via **@Import** on the main application class.
* Component scanning occurs **only if conditions are met** via **@ConditionalOnProperty**.

### Example: JWT Auth Adapter

```java
@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.authentication",
        havingValue = "jwt"
)
public class JwtAuthenticationConfiguration {
}

```

```yaml
# application-[profile].yml
adapter:
  authentication: # jwt
  persistence: # spring-data-jpa | in-memory
  payment: # mock | stripe
  notification: # sms | email
  messaging: # spring-event-bus
  web: # rest
```

## Testing

### Test Catalog and Maven Lifecycle

|     Test Type    | Maven Lifecycle |
| :--------------: | :-------------: |
|     Unit test    |       test      |
|  Component test  |       test      |
|   ArchUnit test  |       test      |
| Integration test |      verify     |


## API Documentation

* Accessible locally at: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


### HTTP Requests
All API requests for testing  can be found in the project under:
```
/http-requests/order-management.http
```

## Docker Setup

* **File:** `docker-compose.yml`
* **Environment variables:** stored in `.env`

Start the database & smtp server:

```bash
docker-compose up -d
```

## 🧪 Testing Payment with Stripe Payment Adapter 

This project integrates with **Stripe Checkout** for payments.  
Follow these steps to test payments locally.

### 1. Create a Stripe Account
1. Go to [https://dashboard.stripe.com/register](https://dashboard.stripe.com/register).
2. Sign up for a free account (no credit card required).
3. Switch to **Test Mode** in the Dashboard.

### 2. Get API Keys
* Navigate to **Developers → API Keys** in the Stripe Dashboard.
* Copy the **Secret Key** (e.g., `sk_test_...`).
* This will be used as `payment.stripe.api-key` in `application.yml`.

### 3. Install Stripe CLI
Install the Stripe CLI from [https://stripe.com/docs/stripe-cli](https://stripe.com/docs/stripe-cli).

Verify installation:

```bash
stripe --version
```

### 4. Forward Webhooks to Your Local App
```bash
stripe listen --forward-to localhost:8080/api/payments/stripe/webhook
```

### 5. Update application.yml
Add your Stripe configuration

```yaml
payment:
  stripe:
    api-key: sk_test_xxxxxxxxxxxxxxxxxxxxx
    webhook-secret: whsec_xxxxxxxxxxxxxxxxxxxxx
    success-url: http://localhost:8080/payment/success/
    failure-url: http://localhost:8080/payment/failure/
```

- api-key → Stripe test secret key from Dashboard.
- webhook-secret → Provided by Stripe CLI when running stripe listen.
- success-url → Redirect URL after successful payment.
- failure-url → Redirect URL after failed/canceled payment.

### 6. Test Payments

Stripe provides a list of test cards for simulating payments:
👉 [https://docs.stripe.com/testing](https://docs.stripe.com/testing)

Common test card:
- Visa (Success): 4242 4242 4242 4242
  - Exp: any future date, CVC: any 3 digits.