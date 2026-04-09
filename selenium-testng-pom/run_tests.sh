#!/bin/bash

# ------------------------------
# run_tests.sh - runs OpenMRS tests for demo environment
# Compatible with Mac/Linux and Azure DevOps hosted agent
# ------------------------------

# Exit on any error
set -e
if [ "$ENV" == "demo" ]; then
  export UI_BASE_URL="https://test3.openmrs.org"
  export API_BASE_URL="https://test3.openmrs.org/openmrs"
  export USERNAME="admin"
  export PASSWORD="Admin123"

elif [ "$ENV" == "docker" ]; then
  export UI_BASE_URL="http://localhost:8082/openmrs"
  export API_BASE_URL="http://localhost:8082/openmrs"
  export USERNAME="admin"
  export PASSWORD="Admin123"

else
  echo "Error: Unsupported environment: $ENV"
  exit 1
fi
# ------------------------------
# Run Maven tests
# ------------------------------
# This will compile (if needed) and run TestNG tests defined in testng.xml
# Clean ensures previous builds do not interfere
echo "Running Maven tests..."
mvn clean test -Denv=$ENV

echo "OpenMRS tests completed successfully for environment: $ENV"