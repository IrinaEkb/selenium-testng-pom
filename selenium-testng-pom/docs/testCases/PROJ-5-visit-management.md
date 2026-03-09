# PROJ-5 — Visit Management 

## User Story

As a healthcare staff (e.g., nurse or registrar)  
I want to create and manage patient visits  
So that patient interactions with the healthcare system are properly recorded.

---

## Business Rules

1. A visit represents a time period during which a patient interacts with the healthcare system.
2. A visit must reference an existing patient.
3. Each visit must have a valid visit type.
4. A visit may contain one or more encounters.
5. Visit start time must be valid ISO date format.
6. A visit may optionally include:
   - location
   - encounters
   - indication
   - attributes

---

## Test Cases

### VISIT-001 Create Visit

**Precondition**  
Patient exists in the system.

**Steps**

1. Prepare visit data including patient, visit type, start time, and optional fields.
2. Submit the data to create a new visit.
3. Verify that a visit UUID is generated.
4. Verify that all provided data is stored correctly.

**Expected**  
Visit is created successfully with all submitted values.

**API Test:**  
POST request to `/visit`
- Expected response: 201, JSON with visit UUID and submitted fields.

---

### VISIT-002 Retrieve Visit by UUID

**Precondition**  
Visit exists.

**Steps**

1. Request visit details using the visit UUID.
2. Verify returned data matches the stored visit.
3. Confirm patient UUID is correct.

**Expected**  
Visit data is retrieved correctly.

**API Test:**  
GET request to `/visit/{uuid}`
- Expected response: 200, JSON with correct visit and patient data.

---

### VISIT-003 List Visits for Patient

**Precondition**  
Patient has at least one visit.

**Steps**

1. Request all visits for the patient.
2. Verify each visit belongs to the correct patient.

**Expected**  
All visits for the patient are returned.

**API Test:**  
GET request to `/visit?patient={uuid}`
- Expected response: 200, JSON array of visits linked to the patient.

---

### VISIT-004 Update Visit

**Precondition**  
Visit exists.

**Steps**

1. Modify one or more visit fields (e.g., startDatetime).
2. Submit updated data.
3. Verify that updated fields are correctly stored.

**Expected**  
Visit is updated successfully.

**API Test:**  
POST request to `/visit/{uuid}`
- Expected response: 200, JSON with updated fields.

---

### VISIT-005 Delete Visit

**Precondition**  
Visit exists.

**Steps**

1. Delete the visit using its UUID.
2. Verify that the visit is removed from the system.

**Expected**  
Visit no longer exists.

**API Test:**  
DELETE request to `/visit/{uuid}`
- Expected response: 200 or 204.

---

### VISIT-006 Retrieve Non-Existing Visit

**Steps**

1. Attempt to retrieve a visit with a non-existent UUID.

**Expected**  
System returns an error indicating visit not found.

**API Test:**  
GET request to `/visit/{uuid}`
- Expected response: 404.

---

### VISIT-007 Create Visit Without Patient

**Steps**

1. Attempt to create a visit without specifying a patient.

**Expected**  
System returns a validation error.

**API Test:**  
POST request to `/visit`
- Expected response: 400, validation error.

---

### VISIT-008 Create Visit With Invalid VisitType

**Steps**

1. Attempt to create a visit with an invalid visit type.

**Expected**  
System returns a validation error.

**API Test:**  
POST request to `/visit`
- Expected response: 400, validation error.

---

### VISIT-009 Create Visit With Invalid Date

**Steps**

1. Attempt to create a visit with an invalid start date format.

**Expected**  
System returns a validation error.

**API Test:**  
POST request to `/visit`
- Expected response: 400, validation error.

---

### VISIT-010 List Visits With Date Filter

**Steps**

1. Request visits filtered by `fromStartDate`.
2. Verify that only visits after the specified date are returned.

**Expected**  
System returns only visits after the given date.

**API Test:**  
GET request to `/visit?fromStartDate={date}`
- Expected response: 200, JSON array of filtered visits.