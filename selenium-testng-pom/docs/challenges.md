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

### ⬜ 1. Checking the file in history

```bash
git log --all -- openmrs.war

✅ Verified that openmrs.war still exists in the repository history even though it was deleted locally.
git rev-list --objects --all | grep openmrs.war
✅ Found the SHA of the file in the history.

⬛ 2. Using git filter-repo to remove the file
git filter-repo --invert-paths --path openmrs.war --force
--invert-paths --path openmrs.war removes the file from all commits.
--force is required because the repo was not a fresh clone.
✅ This rewrote the Git history without the openmrs.war file.

⬜ 3. Verifying removal
git rev-list --objects --all | grep openmrs.war
✅ Confirm the file is no longer in Git history.
git log --all -- openmrs.war
✅ Ensure no commits contain the file.
find . -name "*.war"
✅ Confirm the file does not exist in the working directory.
git check-ignore -v openmrs.war
✅ Verify .gitignore successfully ignores the file for future commits.

⬛ 4. Pushing cleaned history to GitHub
git push origin main --force
✅ Force push was required because the history was rewritten.
After this, GitHub accepted the push without errors related to large files.

✅ Conclusion
Do:
Identify large files in Git history before pushing.
Use git filter-repo to remove them if necessary.
Verify removal locally and ensure .gitignore prevents future commits.
Push the cleaned repository to GitHub.
Don't:
Push repositories containing files larger than 100 MB.
Ignore history when cleaning large files — verification is essential.
Blindly rely on cloning a fresh repo as the only safe method.
