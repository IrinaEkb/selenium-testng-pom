package utils.api;
import utils.ConfigReader;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Factory for creating patient payloads according to OpenMRS API documentation.
 */
public class PatientPayloadFactory {

    // Existing identifier types in the system
    private static final List<String> VALID_IDENTIFIER_TYPES = List.of(
            "05a29f94-c0ed-11e2-94be-8c13b969e334"
    );
    // Example location UUID from system
    private static final String VALID_LOCATION = "302090a6-11ac-455d-80c9-08c06ebd1087";
    private static final List<String> GENDERS = List.of("M", "F", "U");
    // Helper: pick random gender
    private static String randomGender() {
        return GENDERS.get(ThreadLocalRandom.current().nextInt(GENDERS.size()));
    }
    // Helper: pick random identifier type
    private static String randomIdentifierType() {
        return VALID_IDENTIFIER_TYPES.get(
                ThreadLocalRandom.current().nextInt(VALID_IDENTIFIER_TYPES.size())
        );
    }

    // Helper: generate random string of given length
    private static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Helper: generate random birthdate, age 0–115
    private static LocalDate randomBirthdate() {
        int age = ThreadLocalRandom.current().nextInt(0, 116);
        return LocalDate.now().minusYears(age);
    }

    // Helper: generate identifier of given length
    private static String generateIdentifier(int length) {
        // All identifiers are same format (letters/numbers), single type
        return randomString(length);
    }

    // Core builder method
    private static Map<String, Object> buildPayload(String givenName,
                                                    String familyName,
                                                    String gender,
                                                    LocalDate birthdate,
                                                    String identifier,
                                                    String identifierType) {

        Map<String, Object> payload = new HashMap<>();

        // Identifiers
        Map<String, Object> identifierMap = new HashMap<>();
        identifierMap.put("identifier", identifier);
        identifierMap.put("identifierType", identifierType);
        identifierMap.put("location", VALID_LOCATION);
        identifierMap.put("preferred", true);

        payload.put("identifiers", List.of(identifierMap));

        // Person
        Map<String, Object> person = new HashMap<>();
        person.put("gender", gender);
        person.put("birthdate", birthdate.toString());
        person.put("birthdateEstimated", false);
        person.put("dead", false);
        person.put("deathDate", null);
        person.put("causeOfDeath", null);

        // Names
        Map<String, Object> name = new HashMap<>();
        name.put("givenName", givenName);
        name.put("familyName", familyName);
        name.put("preferred", true);
        person.put("names", List.of(name));

        // Addresses
        Map<String, Object> address = new HashMap<>();
        address.put("address1", "123 Main St");
        address.put("cityVillage", "Bengaluru");
        address.put("country", "India");
        address.put("postalCode", "560037");
        person.put("addresses", List.of(address));

        payload.put("person", person);

        return payload;
    }

    // ===== VALID PAYLOAD =====
    public static Map<String, Object> createValidPatient() {
        String givenName = randomString(10);
        String familyName = randomString(10);
        return buildPayload(
                givenName,
                familyName,
                randomGender(),
                randomBirthdate(),
                generateIdentifier(8),
                randomIdentifierType()
        );
    }

    // ===== NEGATIVE: MISSING GENDER =====
    public static Map<String, Object> withoutGender() {
        Map<String, Object> payload = createValidPatient();
        ((Map<String, Object>) payload.get("person")).remove("gender");
        return payload;
    }

    // ===== NEGATIVE: MISSING IDENTIFIERS ARRAY =====
    public static Map<String, Object> missingIdentifiers() {
        Map<String, Object> payload = createValidPatient();
        payload.remove("identifiers");
        return payload;
    }

    // ===== NEGATIVE: IDENTIFIER TOO LONG =====
    public static Map<String, Object> identifierTooLong() {
        Map<String, Object> payload = createValidPatient();
        Map<String, Object> identifierMap = ((List<Map<String, Object>>) payload.get("identifiers")).get(0);
        identifierMap.put("identifier", generateIdentifier(256));
        return payload;
    }

    // ===== NEGATIVE: INVALID IDENTIFIER TYPE =====
    public static Map<String, Object> invalidIdentifierType() {
        Map<String, Object> payload = createValidPatient();
        Map<String, Object> identifierMap = ((List<Map<String, Object>>) payload.get("identifiers")).get(0);
        identifierMap.put("identifierType", UUID.randomUUID().toString());
        return payload;
    }

    // ===== NEGATIVE: FUTURE BIRTHDATE =====
    public static Map<String, Object> birthdateInFuture() {
        Map<String, Object> payload = createValidPatient();
        ((Map<String, Object>) payload.get("person")).put("birthdate", LocalDate.now().plusDays(1).toString());
        return payload;
    }

    // ===== NEGATIVE: MISSING IDENTIFIER TYPE =====
    public static Map<String, Object> missingIdentifierType() {
        Map<String, Object> payload = createValidPatient();
        Map<String, Object> identifierMap =
                ((List<Map<String, Object>>) payload.get("identifiers")).get(0);

        identifierMap.remove("identifierType");

        return payload;
    }

    // ===== NEGATIVE: EMPTY IDENTIFIERS ARRAY =====
    public static Map<String, Object> emptyIdentifiers() {
        Map<String, Object> payload = createValidPatient();
        payload.put("identifiers", new ArrayList<>());
        return payload;
    }

    // ===== AUTO-GENERATION OPTION PAYLOADS =====
    public static class AutoGenerationOptionPayloads {

        private static final String DEFAULT_SOURCE = "78b39363-c19a-48cf-bf0d-1a00009deaeb";
        private static final String DEFAULT_IDENTIFIER_TYPE = "05a29f94-c0ed-11e2-94be-8c13b969e334";
        private static final String DEFAULT_LOCATION = "302090a6-11ac-455d-80c9-08c06ebd1087";

        public static Map<String, Object> validOption() {
            Map<String, Object> payload = new HashMap<>();
            payload.put("source", DEFAULT_SOURCE);
            payload.put("identifierType", DEFAULT_IDENTIFIER_TYPE);
            payload.put("manualEntryEnabled", true);
            payload.put("automaticGenerationEnabled", true);
            payload.put("location", DEFAULT_LOCATION);
            return payload;
        }

        public static Map<String, Object> missingSource() {
            Map<String, Object> payload = validOption();
            payload.remove("source");
            return payload;
        }

        public static Map<String, Object> invalidIdentifierType() {
            Map<String, Object> payload = validOption();
            payload.put("identifierType", UUID.randomUUID().toString());
            return payload;
        }

        public static Map<String, Object> missingLocation() {
            Map<String, Object> payload = validOption();
            payload.remove("location");
            return payload;
        }
    }
}