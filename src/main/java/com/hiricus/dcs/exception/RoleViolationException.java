package com.hiricus.dcs.exception;

public class RoleViolationException extends RuntimeException {
    public RoleViolationException(String message) {
        super(message);
    }
}
