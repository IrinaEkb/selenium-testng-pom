# PROJ-103 — Patient API Validation

## User Story

As a System Administrator  
I want to create, retrieve and update patient profiles  
So that patient medical records are stored and managed correctly in the system.

---

## Business Rules

## Business Rules (Detailed)

1. Patient must contain:
   - givenName (max 25 chars)
   - familyName (max 25 chars)
   - gender (M, F, U)
   - birthdate (not in future, age 0–115)
   - identifiers (at least 1)

2. Identifier must contain:
   - identifier (length 1–255)
   - identifierType (valid existing UUID)
   - location (valid existing UUID)

3. UUID is generated automatically.

4. System returns:
   - 400 for validation errors
   - 404 for non-existing UUID
   - 201 for successful creation

---

## Test Cases

### PATIENT-001 Create Patient
Precondition: Admin is authenticated  
Steps:
1. Send POST request with valid patient payload
   Expected:
- Status 201
- UUID is returned

---

### PATIENT-002 Get Existing Patient
Precondition: Patient exists  
Steps:
1. Send GET request with valid UUID
   Expected:
- Status 200
- UUID matches created patient

---

### PATIENT-003 Update Patient
Precondition: Patient exists  
Steps:
1. Send POST update request
   Expected:
- Status 200
- Data updated successfully

---

### PATIENT-004 Missing Gender
Steps:
1. Send POST request without gender
   Expected:
- Status 400

---

### PATIENT-005 Non-existing UUID
Steps:
1. Send GET request with random UUID
   Expected:
- Status 404
- 
### PATIENT-006 Birthdate In Future

Steps:
1. Send POST request with birthdate = tomorrow

Expected:
- Status 400

---

### PATIENT-007 Missing Identifier Type

Steps:
1. Send POST request to create patient
2. Provide identifier object without `identifierType`

Expected:
- Status 400
- Validation error returned

---

### PATIENT-008 Identifier Too Long

Steps:
1. Send POST request with identifier length = 256

Expected:
- Status 400

---

### PATIENT-009 Invalid IdentifierType UUID

Steps:
1. Send POST request with random UUID for identifierType

Expected:
- Status 400

---
### PATIENT-010 Empty Identifiers Array

Steps:
1. Send POST request to create patient
2. Provide empty identifiers array

Example payload:

"identifiers": []

Expected:

Status 400

Validation error returned

---

### PATIENT-011 Missing Identifiers Field

Steps:
1. Send POST request to create patient
2. Remove `identifiers` field from payload

Expected:
- Status 400
- Validation error returned
