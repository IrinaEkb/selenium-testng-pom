# Bug Report: PATIENT-006 — Birthdate In Future

**Summary:**  
Creating a patient with a birthdate in the future via the API should return `400 Bad Request`. Currently, this validation **fails in the Docker environment**, while it works in demo/test environments.

**Environment:**
- **Docker (local)**: openmrs-backend:3.6.0, MySQL 8, Java 17
- **Demo/Test3**: https://test3.openmrs.org, openmrs-core 2.x, MySQL 5.7
- **OS**: MacOS 13, Ubuntu 22.04
- **API Client**: RestAssured via TestNG

**Steps to Reproduce:**
1. Send POST request to `/patient` endpoint with payload containing `"birthdate": "2100-01-01"`.
2. Observe response status and body.

**Expected Result:**
- API returns `400 Bad Request`
- Response body contains validation error for `birthdate`.

**Actual Result:**
- **Docker/local environment:** Returns `201 Created` and patient is inserted in DB (incorrect)
- **Demo/Test3 environment:** Correctly returns `400 Bad Request`

**Payload Example:**
```json
{
  "name": "Future Baby",
  "gender": "F",
  "birthdate": "2100-01-01",
  "identifiers": [{"identifier": "12345", "identifierType": "uuid"}]
}