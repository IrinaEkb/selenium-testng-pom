# PROJ-102 — OpenMRS API Login Session Validation

## Story Description

As a QA Engineer  
I want to verify user authentication via the OpenMRS REST session API  
So that backend login works correctly independent of the UI

---

## Acceptance Criteria

- Session endpoint returns HTTP 200 for valid credentials
- `authenticated = true` for valid credentials
- `authenticated = false` or HTTP 401 for invalid credentials
- `sessionId` is present after successful login

---

## Test Cases

### API-LOGIN-001 — Verify Login With Valid Credentials

Type: Smoke  
Priority: High

Preconditions:
- Demo server is available
- REST module is installed

Steps:
1. Send GET request to `/ws/rest/v1/session`
2. Add Basic Auth header: `admin / Admin123`

Expected Result:
- Status code = 200
- `authenticated = true`
- `sessionId` exists

---

### API-LOGIN-002 — Verify Login With Invalid Password

Type: Negative  
Priority: High

Steps:
1. Send GET request to `/ws/rest/v1/session`
2. Add Basic Auth header with wrong password

Expected Result:
- `authenticated = false` OR status code = 401

---

### API-LOGIN-003 — Verify Session Endpoint Without Auth

Type: Negative  
Priority: Medium

Steps:
1. Call session endpoint `/ws/rest/v1/session` **without Authorization header**

Expected Result:
- Status code = 401 OR `authenticated = false`