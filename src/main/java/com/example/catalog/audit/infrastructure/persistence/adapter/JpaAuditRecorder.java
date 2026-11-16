package com.example.catalog.audit.infrastructure.persistence.adapter;

import com.example.catalog.audit.application.port.AuditRecorder;
import com.example.catalog.audit.infrastructure.persistence.entity.AuditRecordEntity;
import com.example.catalog.audit.infrastructure.persistence.repository.AuditRecordRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaAuditRecorder implements AuditRecorder {
    private final AuditRecordRepository repository;

    public JpaAuditRecorder(AuditRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void record(String actor, String action, String resourceType, String resourceId) {
        repository.save(new AuditRecordEntity(actor, action, resourceType, resourceId));
    }
}
