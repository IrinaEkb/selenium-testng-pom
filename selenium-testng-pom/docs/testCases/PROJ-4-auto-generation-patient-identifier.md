# PROJ-4 — AutoGenerationOption Patient Identifier

## User Story

As a healthcare staff (e.g., nurse or registrar)  
I want to create, retrieve, update, and delete AutoGenerationOptions  
So that patient identifiers are generated correctly according to the system configuration.

---

## Business Rules

1. Each AutoGenerationOption must reference an existing IdentifierSource.
2. Each AutoGenerationOption must reference a valid PatientIdentifierType.
3. Manual entry and automatic generation can be enabled or disabled independently.
4. AutoGenerationOption may optionally be restricted to a specific location.
5. Creating or updating an option with missing or invalid required fields must return a validation error.
6. Deleting an AutoGenerationOption removes it completely; it cannot be retired or voided.

---

## Test Cases

### AUTO-001 Create AutoGenerationOption

**Precondition**  
IdentifierSource and PatientIdentifierType exist.

**Steps**

1. Prepare payload with valid source UUID and identifier type UUID.
2. Set `manualEntryEnabled` and `automaticGenerationEnabled` flags.
3. Optionally set a location UUID.
4. Submit POST request to `/idgen/autogenerationoption`.
5. Verify response contains a UUID.
6. Verify all submitted fields are correctly stored in the system.

**Expected**  
AutoGenerationOption is created successfully with provided values.

**API Test:** POST request returns 201 with UUID and correct fields.

---

### AUTO-002 Retrieve AutoGenerationOption by UUID

**Precondition**  
AutoGenerationOption exists.

**Steps**

1. Use the UUID of an existing option.
2. Submit GET request to `/idgen/autogenerationoption/:uuid`.
3. Verify response contains the correct UUID.
4. Verify all fields match the created AutoGenerationOption.

**Expected**  
Option is retrieved with correct values.

**API Test:** GET request returns 200 with correct UUID and fields.

---

### AUTO-003 Update AutoGenerationOption

**Precondition**  
AutoGenerationOption exists.

**Steps**

1. Select an existing option UUID.
2. Modify `manualEntryEnabled` or `automaticGenerationEnabled`.
3. Optionally update the location.
4. Submit POST request to `/idgen/autogenerationoption/:uuid`.
5. Verify updated fields in the response.
6. Verify updated values are correctly stored in the system.

**Expected**  
Option fields updated correctly.

**API Test:** POST request returns 200 with updated fields.

---

### AUTO-004 Delete AutoGenerationOption

**Precondition**  
AutoGenerationOption exists.

**Steps**

1. Select an existing option UUID.
2. Submit DELETE request to `/idgen/autogenerationoption/:uuid?purge=true`.
3. Attempt to retrieve the option by UUID.

**Expected**  
Option no longer exists in the system.

**API Test:** DELETE request returns 204; subsequent GET returns 404.

---

### AUTO-005 Create AutoGenerationOption with Missing Source

**Steps**

1. Prepare payload without `source` field.
2. Include valid identifier type UUID and flags.
3. Submit POST request to `/idgen/autogenerationoption`.

**Expected**  
System returns a validation error; option is not created.

**API Test:** POST request returns 400.

---

### AUTO-006 Create AutoGenerationOption with Invalid IdentifierType

**Steps**

1. Prepare payload with invalid identifier type UUID.
2. Include valid source UUID and flags.
3. Submit POST request to `/idgen/autogenerationoption`.

**Expected**  
System returns a validation error; option is not created.

**API Test:** POST request returns 400.

---

### AUTO-007 Create AutoGenerationOption without Location

**Precondition**  
IdentifierSource and PatientIdentifierType exist.

**Steps**

1. Prepare payload with valid source UUID and identifier type UUID.
2. Set flags for manual and automatic generation.
3. Do not include location.
4. Submit POST request to `/idgen/autogenerationoption`.
5. Verify option is created successfully.
6. Verify `location` field is empty or null.

**Expected**  
Option is created without a location.

**API Test:** POST request returns 201 with location field null.