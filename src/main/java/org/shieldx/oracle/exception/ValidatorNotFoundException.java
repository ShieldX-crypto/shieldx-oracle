package org.shieldx.oracle.exception;

public class ValidatorNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Validator not found: %s";
    public ValidatorNotFoundException(String address) {
        super(String.format(MESSAGE, address));
    }
}
