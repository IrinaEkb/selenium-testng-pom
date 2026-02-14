# Challenge: `openmrs.war` in Git History (Large file >100MB)

---

## Problem
- The file `openmrs.war` (>100MB) was accidentally committed **before being added to `.gitignore`**.
- Even though the file was **not visible in the framework** (it lived in the project folder for Docker), `git add .` included it because **Git tracks everything inside the repo folder**.
- `.gitignore` only prevents **future additions**, it does **not remove files already in Git history**.
- **GitHub rejects pushes larger than 100MB**, so pushes were failing.
- The file was referenced in `docker-compose.yml` (MySQL + Tomcat setup), which could cause it to reappear if not properly handled.

---

## Solution

### Step 1️⃣ — Fresh clone
Create a clean copy of the repository:
```bash
git clone <repo-url> clean-repo
cd clean-repo

Step 2️⃣ — Remove from history
Remove openmrs.war from all Git commits using git-filter-repo:
git filter-repo --path openmrs.war --invert-paths --force

Step 3️⃣ — Add to .gitignore
Prevent future accidental commits:
echo "openmrs.war" >> .gitignore
git add .gitignore
git commit -m "Ignore openmrs.war to prevent future commits"

Step 4️⃣ — Push once with force
Update GitHub safely with the cleaned history:
git push origin main --force

Step 5️⃣ — Verify
Make sure Git ignores the file and the history is clean:
git check-ignore -v openmrs.war
git status

✅Result / Takeaways
openmrs.war is removed from Git history.
.gitignore prevents future accidental commits.
Future pushes work normally, without exceeding GitHub file size limits.
Best practice: Always review large or external files before committing, especially those referenced in Docker or configuration files.
