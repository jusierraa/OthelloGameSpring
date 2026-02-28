package com.othello.exception;

/**
 * Excepción lanzada cuando hay un conflicto de versión (concurrencia optimista).
 */
public class VersionConflictException extends RuntimeException {
    private final Long expectedVersion;
    private final Long currentVersion;

    public VersionConflictException(Long expectedVersion, Long currentVersion) {
        super(String.format("Conflicto de versión: esperada %d, actual %d", expectedVersion, currentVersion));
        this.expectedVersion = expectedVersion;
        this.currentVersion = currentVersion;
    }

    public Long getExpectedVersion() {
        return expectedVersion;
    }

    public Long getCurrentVersion() {
        return currentVersion;
    }
}
