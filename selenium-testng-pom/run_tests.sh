#!/bin/bash

# ------------------------------
# run_tests.sh - runs OpenMRS tests for demo environment
# Compatible with Mac/Linux and Azure DevOps hosted agent
# ------------------------------

# Exit on any error
set -e

# Get environment argument (default to demo)
ENV=${1:-demo}
echo "Starting OpenMRS tests for environment: $ENV"

# ------------------------------
# Set demo server variables
# ------------------------------
if [ "$ENV" == "demo" ]; then
  export UI_BASE_URL="https://openmrs.org"
  export API_BASE_URL="https://o3.openmrs.org/openmrs"
  export USERNAME="admin"
  export PASSWORD="Admin123"
else
  echo "Error: Unsupported environment: $ENV"
  exit 1
fi

echo "UI_BASE_URL=$UI_BASE_URL"
echo "API_BASE_URL=$API_BASE_URL"

# ------------------------------
# Run Maven tests
# ------------------------------
# This will compile (if needed) and run TestNG tests defined in testng.xml
# Clean ensures previous builds do not interfere
echo "Running Maven tests..."
mvn clean test -Denv=$ENV

echo "OpenMRS tests completed successfully for environment: $ENV"