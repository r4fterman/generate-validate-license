package org.r4fter.licensing.validation;

public class CannotValidateLicenseException extends Exception {

    public CannotValidateLicenseException(String message, Exception exception) {
        super(message, exception);
    }
}
