package com.example.catalog.audit.infrastructure.persistence.repository;

import com.example.catalog.audit.infrastructure.persistence.entity.AuditRecordEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRecordRepository extends JpaRepository<AuditRecordEntity, Long> {}
