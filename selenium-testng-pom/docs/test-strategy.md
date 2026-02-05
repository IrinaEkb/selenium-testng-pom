# Test Strategy — OpenMRS EMR Automation Project

---

## Overview and Scope

This test strategy defines the high-level testing approach for the OpenMRS Electronic Medical Record (EMR) automation learning project.

OpenMRS is an open-source healthcare system used to manage patient records, visits, and clinical data. Testing focuses on validating core patient workflows across UI, API, and database layers.

This test strategy establishes testing objectives, scope, environments, tools, quality controls, and reporting practices. The strategy is a stable, high-level document that serves as a baseline for creating detailed Test Plans and Test Cases.

---

## Scope

### In Scope
- User authentication
- Patient registration and search
- Patient profile data
- Visit management
- Clinical data (vitals, allergies, diagnoses)
- API data operations
- Database data storage and consistency validation

### Out of Scope
- Infrastructure setup (CI/CD, cloud, container orchestration — planned for future phases)
- External third-party integrations not available in demo environment

---

## Testing Objectives

- Validate core EMR workflows
- Ensure correct data storage and retrieval across system layers
- Verify business rules for healthcare data handling (HIPAA-aware validation rules where applicable)
- Maintain stability of existing functionality through regression automation
- Identify functional and data-related defects

---

## Test Approach

Testing follows an automation-first approach with priority on end-to-end workflow validation.

### Framework Design Principles
- Page Object Model (POM)
- BaseTest for driver lifecycle and configuration
- BasePage for shared page actions and common UI behavior
- Thread-safe driver handling for parallel execution
- Reusable helper layer for waits, logging, configuration, and test data support

Automation is implemented using TestNG and Maven.

### Test Data Strategy
- Use dedicated test patient data
- Generate unique data where required
- Avoid reliance on shared demo records

---

## Test Levels

### System Testing
Validates complete patient workflows and clinical data operations from UI through backend layers.

### Integration Testing
Validates communication between UI, API services, and database.

---

## Test Types

### Functional Testing
Validates system behavior against functional requirements and expected workflows.

### Regression Testing
Ensures existing functionality remains stable after updates or configuration changes.

### Smoke Testing
Validates core system stability after deployment or environment refresh.

### API Testing
Validates service contracts, request and response structures, authentication behavior, business rules, and data persistence triggered by API operations.

### Data and Database Validation Testing
Validates data correctness, completeness, and consistency between UI input, API processing, and database storage, including verification of business data rules.

---

## Environment Requirements

Testing is executed in controlled test or demo environments.

Environment setup includes:
- OpenMRS demo instance
- Chrome browser
- Safari browser
- Java 21 runtime
- MySQL database
- Dedicated test data

---

## Testing Tools

### Automation Stack
- Java 21
- Selenium WebDriver
- TestNG
- Maven

### Supporting Framework Components
- WebDriverManager (driver binary management)
- JDBC (database validation)
- Allure Reports (test reporting)
- Logging framework

### Test Management and Defect Tracking
- Jira or similar tracking system

---

## Standards and Compliance Considerations

Testing follows general software quality assurance best practices and ISTQB testing principles.

Healthcare-related testing validates data handling rules and access behavior according to healthcare data protection expectations using non-production test data.

---

## Entry Criteria

Testing can begin when:
- Test environment is available
- Application build is deployed
- Test data is prepared
- Automation scripts or test cases are ready

---

## Exit Criteria

Testing is complete when:
- Core workflows are validated
- No critical or high severity defects remain open
- Regression execution meets expected pass rate
- Test reporting is completed

---

## Test Deliverables

- Test Strategy
- Test Plan
- Automated Test Scripts
- Test Execution Reports
- Defect Reports
- Test Summary Reports
- Requirement Traceability Mapping

---

## Testing Metrics

- Automation coverage
- Test execution progress
- Defect density
- Defect severity distribution
- Regression pass rate
- Defect reopen rate

---

## Requirement Traceability

Requirements are mapped to test cases to ensure coverage of functional and business requirements.

---

## Risk and Mitigation

### Risks
- Shared demo environment data changes
- Environment instability
- UI locator changes
- Limited control over demo dataset

### Mitigation
- Maintain stable test data sets
- Use resilient locator strategies
- Maintain regression automation coverage
- Validate environment health before execution

---

## Reporting

Testing status and defects are tracked using:
- Automation reports (Allure)
- Defect tracking system
- Test execution summaries per test cycle

---

## Test Summary

Test summary reports are generated per execution cycle and include execution results, defect status, risk areas, and overall quality status.