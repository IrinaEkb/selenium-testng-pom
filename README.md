# OpenMRS QA Automation Project

## Overview

This project implements **automated UI and API tests** for OpenMRS.

### Framework / Approaches

- **TestNG** – test framework for structuring tests, running them in groups, and parallel execution
- **Page Object Model (POM)** – each page has a class with locators and actions, making tests easier to maintain
- **Data-Driven Testing** – test data is stored externally (Excel, JSON) for flexible testing
- **Listeners & Screenshots** – automatically capture logs and screenshots on test failures
- **DriverManager** – custom utility to create and manage WebDriver instances for different browsers (Chrome, Safari)

### Tools / Libraries

- **Selenium 4** – automates browser interactions for UI testing
- **Allure** – generates test reports and dashboards
- **REST-assured** – API testing
- **Apache POI** – reading/writing Excel files for data-driven tests
- **Log4j2 / SLF4J** – logging test execution

---

## Environments
Project supports 3 environments:

-Denv=local # OpenMRS via SDK
-Denv=docker # OpenMRS in Docker
-Denv=demo # Public demo server

Choose one environment and follow the corresponding setup.
---
# 1. Docker

## Requirements
- Docker
- Docker Compose

## Setup
```bash
git clone https://github.com/IrinaEkb/selenium-testng-pom.git
cd selenium-testng-pom
docker compose up
```
Application URL
http://localhost:8082/openmrs/

Credentials
admin / Admin123

Run tests
```bash
mvn test -Denv=docker
```

# 2. Local (OpenMRS SDK)
## Requirements
- Java 21
- Maven
- OpenMRS SDK

## Setup
```bash
git clone https://github.com/IrinaEkb/selenium-testng-pom.git
cd selenium-testng-pom
mvn openmrs-sdk:run
```
Application URL
http://localhost:8081/openmrs/

Run tests
```bash
mvn test -Denv=local
```

# 3. Public Demo
## Requirements
- Java 21
- Maven

## Setup
```bash
git clone https://github.com/IrinaEkb/selenium-testng-pom.git
cd selenium-testng-pom
```
Run tests
```bash
mvn test -Denv=demo
```