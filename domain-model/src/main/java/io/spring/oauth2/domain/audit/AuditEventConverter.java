package io.spring.oauth2.domain.audit;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Optional.ofNullable;

@Component
public class AuditEventConverter {

    /**
     * Convert a list of PersistentAuditEvent to a list of AuditEvent
     *
     * @param persistentAuditEvents the list to convert
     * @return the converted list.
     */
    public List<AuditEvent> convertToAuditEvent(Iterable<PersistentAuditEvent> persistentAuditEvents) {
        if (persistentAuditEvents == null) {
            return newArrayList();
        }
        List<AuditEvent> auditEvents = newArrayList();
        for (PersistentAuditEvent persistentAuditEvent : persistentAuditEvents) {
            auditEvents.add(convertToAuditEvent(persistentAuditEvent));
        }
        return auditEvents;
    }

    /**
     * Convert a PersistentAuditEvent to an AuditEvent
     *
     * @param persistentAuditEvent the event to convert
     * @return the converted list.
     */
    public AuditEvent convertToAuditEvent(PersistentAuditEvent persistentAuditEvent) {
        Instant instant = persistentAuditEvent.getAuditEventDate().atZone(ZoneId.systemDefault()).toInstant();
        return new AuditEvent(Date.from(instant), persistentAuditEvent.getPrincipal(),
                persistentAuditEvent.getAuditEventType(), convertDataToObjects(persistentAuditEvent.getData()));
    }

    /**
     * Internal conversion. This is needed to support the current SpringBoot actuator AuditEventRepository interface
     *
     * @param data the data to convert
     * @return a map of String, Object
     */
    public Map<String, Object> convertDataToObjects(Map<String, String> data) {
        final Map<String, Object> results = newHashMap();
        ofNullable(data).ifPresent(d -> {
            for (String key : data.keySet()) {
                results.put(key, data.get(key));
            }
        });
        return results;
    }

    /**
     * Internal conversion. This method will allow to save additional data.
     * By default, it will save the object as string
     *
     * @param data the data to convert
     * @return a map of String, String
     */
    public Map<String, String> convertDataToStrings(Map<String, Object> data) {
        final Map<String, String> results = newHashMap();
        ofNullable(data).ifPresent(d -> d.keySet().forEach(key -> {
            Object object = data.get(key);
            // Extract the data that will be saved.
            if (object instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails) object;
                results.put("remoteAddress", authenticationDetails.getRemoteAddress());
                results.put("sessionId", authenticationDetails.getSessionId());
            } else if (object != null) {
                results.put(key, object.toString());
            } else {
                results.put(key, "null");
            }
        }));
        return results;
    }
}
