# OpenMRS QA Automation Project

## Overview

This project focuses on **validating critical OpenMRS functionalities** to ensure the system supports reliable patient care workflows.  
Tests cover core business areas that are **essential for operational integrity**, including patient management, visit tracking, identifier generation, and authentication.

---

## Tested Areas

### Patient Management
Patient data is the **core of OpenMRS**. Tests cover creating, updating, and retrieving patients to ensure that all information is captured correctly, mandatory fields are enforced, and duplicate or inconsistent records are prevented.

### Patient Identifiers (AutoGenerationOption)
Unique identifiers are critical for **accurate patient identification**. Automated tests validate that identifiers are generated according to business rules, ensuring reliable patient records and preventing duplicates.

### Visit Management
Visits represent patient interactions and clinical events. Testing visit creation, updates, and retrieval ensures that **clinical workflows are correctly tracked** and reporting reflects actual patient activity.

### Authentication & Session
Secure login and session management protect patient data and maintain compliance with privacy standards. Tests verify that only authorized users can access the system and that session handling is reliable.

---

## Framework / Approaches

- **TestNG** – structures tests into groups and supports parallel execution  
- **Page Object Model (POM)** – encapsulates page locators and actions for maintainability  
- **Data-Driven Testing** – uses external data (Excel, JSON) to cover real-world scenarios  
- **Listeners & Screenshots** – automatically capture logs and screenshots on failures  
- **DriverManager** – manages WebDriver instances for different browsers (Chrome, Safari)

---

## Tools / Libraries

- **Selenium 4** – UI automation  
- **REST-assured** – API testing  
- **Allure** – test reports and dashboards  
- **Apache POI** – Excel file handling  
- **Log4j2 / SLF4J** – logging

---

## Environments

Project supports 2 main environments:

- **Docker** – OpenMRS in Docker for controlled, repeatable tests  
- **Public Demo** – OpenMRS demo server for verifying compatibility with public instance

### Docker
```bash
git clone https://github.com/IrinaEkb/selenium-testng-pom.git
cd selenium-testng-pom
docker compose up
# Application URL: http://localhost:8082/openmrs/
mvn test -Denv=docker

### Demo
git clone https://github.com/IrinaEkb/selenium-testng-pom.git
cd selenium-testng-pom
# Application URL: https://openmrs.org/demo
mvn test -Denv=demo
