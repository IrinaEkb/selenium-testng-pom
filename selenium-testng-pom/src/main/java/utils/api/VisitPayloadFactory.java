package utils.api;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VisitPayloadFactory {

    private static final String VISIT_TYPE = "7b0f5697-27e3-40c4-8bae-f4049abfb4ed";
    private static final String LOCATION = "aff27d58-a15c-49a6-9beb-d30dcfc0c66e";

    private static String now() {

        return ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
    }

    private static Map<String, Object> base(String patientUuid) {
        Map<String, Object> body = new HashMap<>();
        body.put("patient", patientUuid);
        body.put("visitType", VISIT_TYPE);
        body.put("startDatetime", now());
        body.put("location", LOCATION);
        return body;
    }

    // positive
    public static Map<String, Object> valid(String patientUuid) {
        return base(patientUuid);
    }

    public static Map<String, Object> updateStartDate() {
        Map<String, Object> body = new HashMap<>();
        body.put("startDatetime", "2025-01-01T10:00:00.000+0000");
        return body;
    }

    // negative
    public static Map<String, Object> withoutPatient(String patientUuid) {
        Map<String, Object> body = base(patientUuid);
        body.remove("patient");
        return body;
    }

    public static Map<String, Object> invalidVisitType(String patientUuid) {
        Map<String, Object> body = base(patientUuid);
        body.put("visitType", UUID.randomUUID().toString());
        return body;
    }

    public static Map<String, Object> invalidDate(String patientUuid) {
        Map<String, Object> body = base(patientUuid);
        body.put("startDatetime", "INVALID_DATE");
        return body;
    }
}