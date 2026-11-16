package com.example.catalog.audit.application.port;

public interface AuditRecorder {
    void record(String actor, String action, String resourceType, String resourceId);
}
