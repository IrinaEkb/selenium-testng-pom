# PROJ-101 — OpenMRS Demo Page Should Be Accessible

## Story Description

As a user  
I want to open the OpenMRS demo page and access available demo systems  
So that I can explore EMR functionality

---

## Test Cases

### UI-001 — Verify Demo Page Loads

Type: Smoke  
Priority: High

Preconditions:
- User has internet access
- Demo site is available

Steps:
1. Navigate to OpenMRS Demo page

Expected Result:
- Demo page loads successfully
- Demo title is visible

---

### UI-002 — Verify Explore OpenMRS 3 Opens Login

Type: Smoke  
Priority: High

Steps:
1. Open Demo page
2. Click "Explore OpenMRS 3"

Expected Result:
- Login page is opened

---

### UI-003 — Verify Explore OpenMRS 2 Opens Login

Type: Smoke  
Priority: High

Steps:
1. Open Demo page
2. Click "Explore OpenMRS 2"

Expected Result:
- Login page is opened