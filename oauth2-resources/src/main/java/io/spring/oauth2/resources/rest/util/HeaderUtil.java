package io.spring.oauth2.resources.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for http header creation.
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-essportalApp-alert", message);
        headers.add("X-essportalApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("essportalApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("essportalApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("essportalApp." + entityName + ".deleted", param);
    }
}
