# PROJ-2 — Login Session Validation

### User Story

As a QA Engineer  
I want to verify authentication through the OpenMRS REST session API  
So that backend authentication works correctly for all system users.

---

## Business Rules

1. Authentication uses BASIC authentication.
2. Authorization header must contain Base64 encoded `username:password`.
3. Successful authentication returns:
    - HTTP 200
    - `authenticated = true`
    - `sessionId`
4. Invalid credentials return:
    - HTTP 401 OR `authenticated = false`
5. Session token must be returned in the response.
6. The token is later used as cookie `JSESSIONID`.

---

# Test Cases

---

### API-LOGIN-001 Verify Login With Valid Credentials

**Precondition**

Valid admin credentials exist.

**Steps**

1. Send request to retrieve session.
2. Provide Authorization header with valid credentials.
3. Verify authentication status.
4. Verify user information is returned.

**Expected**

User is successfully authenticated and session is created.

**API Test**

GET `/session`

Expected:
- Status 200
- `authenticated = true`
- `sessionId` exists
- `user.uuid` exists

---

### API-LOGIN-002 Verify Login With Invalid Password

**Steps**

1. Send request to retrieve session.
2. Provide Authorization header with incorrect password.

**Expected**

Authentication fails.

**API Test**

GET `/session`

Expected:
- Status 401 OR
- `authenticated = false`

---

### API-LOGIN-003 Verify Session Endpoint Without Auth

**Steps**

1. Call session endpoint without Authorization header.

**Expected**

Request is rejected.

**API Test**

GET `/session`

Expected:
- Status 401 OR
- `authenticated = false`

---

# Additional Tests From Documentation

---

### API-LOGIN-004 Verify Session Token Returned

**Steps**

1. Authenticate using valid credentials.
2. Inspect response headers.

**Expected**

Response contains session cookie.

**API Test**

GET `/session`

Expected:
- `Set-Cookie`
- `JSESSIONID` present

---

### API-LOGIN-005 Verify Logout

**Precondition**

User is authenticated.

**Steps**

1. Authenticate user.
2. Send logout request.

**Expected**

Session is terminated.

**API Test**

DELETE `/session`

Expected:
- Status 200

---

### API-LOGIN-006 Verify Access With Session Cookie

**Precondition**

User is authenticated.

**Steps**

1. Authenticate using Basic Auth.
2. Extract `JSESSIONID`.
3. Send another API request using cookie.

**Expected**

Request is authorized.

**API Test**

GET `/user?q=admin`

Expected:
- Status 200
- user list returned

---

### API-LOGIN-007 Verify Password Change By User

**Precondition**

User is authenticated.

**Steps**

1. Send request to change password.
2. Provide old password.
3. Provide new password.

**Expected**

Password changes successfully.

**API Test**

POST `/password`

Payload


{
"oldPassword": "Admin123",
"newPassword": "Admin4561"
}


Expected:
- Status 200

---

### API-LOGIN-008 Verify Login With New Password

**Precondition**

Password was changed.

**Steps**

1. Authenticate with new password.

**Expected**

Authentication succeeds.

**API Test**

GET `/session`

Expected:
- Status 200
- `authenticated = true`

---

### API-LOGIN-009 Verify Password Change Without Number

**Steps**

1. Attempt to change password without numeric character.

**Expected**

Validation error.

**API Test**

POST `/password`

Expected:
- Status 400 OR validation error

---

### API-LOGIN-010 Verify Login Locations Without Authentication

**Steps**

1. Request login locations without authentication.

**API Test**

GET `/location?tag=Login+Location`

Expected:
- Status 200
- list of login locations returned

---

### API-LOGIN-011 Verify Unauthorized User Access

**Steps**

1. Call protected endpoint without authentication.

**API Test**

GET `/user?q=admin`

Expected:
- Status 401