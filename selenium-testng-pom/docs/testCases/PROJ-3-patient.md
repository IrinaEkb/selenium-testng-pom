# PROJ-3 — Patient Validation

## User Story

As a healthcare staff (e.g., nurse or registrar) 
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

**Precondition**  
Admin is authenticated.

**Steps**

1. Prepare valid patient payload including person details and at least one identifier.
2. Submit request to create a new patient.
3. Verify that a patient UUID is returned.
4. Verify that all patient data is stored correctly.

**Expected**  
Patient is created successfully with all provided details.

**API Test:**  
POST request to `/patient`
- Expected response: 201, JSON with patient UUID and submitted fields.

---

### PATIENT-002 Retrieve Existing Patient

**Precondition**  
Patient exists.

**Steps**

1. Request patient data using the patient UUID.
2. Verify returned data matches the stored patient.

**Expected**  
Patient data is retrieved correctly.

**API Test:**  
GET request to `/patient/{uuid}`
- Expected response: 200, JSON with correct patient and identifiers.

---

### PATIENT-003 Update Patient

**Precondition**  
Patient exists.

**Steps**

1. Modify one or more patient fields (e.g., address or identifiers).
2. Submit update request.
3. Verify updated fields are correctly stored.

**Expected**  
Patient is updated successfully.

**API Test:**  
POST request to `/patient/{uuid}`
- Expected response: 200, JSON with updated patient fields.

---

### PATIENT-004 Missing Gender

**Steps**

1. Attempt to create patient without specifying gender.

**Expected**  
System returns validation error.

**API Test:**  
POST request to `/patient`
- Expected response: 400, validation error.

---

### PATIENT-005 Non-Existing UUID

**Steps**

1. Attempt to retrieve patient using a random UUID.

**Expected**  
System indicates patient not found.

**API Test:**  
GET request to `/patient/{uuid}`
- Expected response: 404.

---

### PATIENT-006 Birthdate In Future

**Steps**

1. Attempt to create patient with birthdate set in the future.

**Expected**  
System returns validation error.

**API Test:**  
POST request to `/patient`
- Expected response: 400, validation error.

---

### PATIENT-007 Missing Identifier Type

**Steps**

1. Attempt to create patient with an identifier missing `identifierType`.

**Expected**  
System returns validation error.

**API Test:**  
POST request to `/patient`
- Expected response: 400, validation error.

---

### PATIENT-008 Identifier Too Long

**Steps**

1. Attempt to create patient with identifier longer than 255 characters.

**Expected**  
System returns validation error.

**API Test:**  
POST request to `/patient`
- Expected response: 400, validation error.

---

### PATIENT-009 Invalid IdentifierType UUID

**Steps**

1. Attempt to create patient with a random UUID for `identifierType`.

**Expected**  
System returns validation error.

**API Test:**  
POST request to `/patient`
- Expected response: 400, validation error.

---

### PATIENT-010 Empty Identifiers Array

**Steps**

1. Attempt to create patient with empty identifiers array.

**Expected**  
System returns validation error.

**API Test:**  
POST request to `/patient` with `"identifiers": []`
- Expected response: 400, validation error.

---

### PATIENT-011 Missing Identifiers Field

**Steps**

1. Attempt to create patient without the `identifiers` field.

**Expected**  
System returns validation error.

**API Test:**  
POST request to `/patient`
- Expected response: 400, validation error.