package io.spring.oauth2.resources.service;

import io.spring.oauth2.domain.audit.AuditEventConverter;
import io.spring.oauth2.domain.audit.PersistenceAuditEventRepository;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
@Transactional(readOnly = true)
public class AuditEventService {

    private AuditEventConverter converter;
    private PersistenceAuditEventRepository repository;

    @Inject
    public AuditEventService(PersistenceAuditEventRepository repository, AuditEventConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public List<AuditEvent> findAll() {
        return converter.convertToAuditEvent(repository.findAll());
    }

    public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
        return converter.convertToAuditEvent(
                repository.findAllByAuditEventDateBetween(fromDate, toDate));
    }

    public Optional<AuditEvent> find(Long id) {
        return ofNullable(repository.findOne(id)).map(converter::convertToAuditEvent);
    }
}
