# Challenge: API Login Test Failing 

---

## Problem

description = "[API-LOGIN-001] Valid login",
groups = {"smoke","api"}

public void verifyValidLogin()
was failing.
Console error
Expected :true
Actual   :false
Later:
AssertionError: expected object to not be null
The test expected:
authenticated = true
sessionId not null
But the response did not match expectations.

```java

What I Did to Debug
1️⃣ Verified credentials
Checked values loaded from configuration:
System.out.println("API username: " + ConfigReader.get("api.username"));
System.out.println("Password length: " + ConfigReader.get("api.password").length());
✔ Found incorrect credentials in config
✔ Fixed username/password
2️⃣ Verified API endpoint manually
curl -v -u admin:Admin123 https://demo.openmrs.org/openmrs/ws/rest/v1/session
Discovered:
Server returned 302 redirect
Redirected to:
https://o3.openmrs.org/openmrs/ws/rest/v1/session
The original endpoint was not returning proper JSON.

* Root Cause
Wrong credentials in config
Wrong API base URL (redirect instead of JSON)
Assertion expected sessionId, but the API does not return it
* Fix
Updated config:
api.base.url=https://o3.openmrs.org/openmrs
api.username=admin
api.password=Admin123

Removed invalid sessionId assertion.
Kept validation for authenticated = true.

Result
✔ API returns correct JSON
✔ authenticated = true
✔ Test passes
