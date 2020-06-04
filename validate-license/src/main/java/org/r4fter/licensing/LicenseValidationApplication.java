package org.r4fter.licensing;

import org.r4fter.licensing.validation.CannotValidateLicenseException;
import org.r4fter.licensing.validation.LicenseValidator;

public class LicenseValidationApplication {

    public static void main(final String[] args) {
        final String userName = "user@email.com";
        String signedLicense = args[0];

        try {
            final LicenseValidator licenseValidator = new LicenseValidator();
            final boolean valid  = licenseValidator.validate(userName, signedLicense);

            print(userName + "\n" + valid);
        } catch (CannotValidateLicenseException e) {
            e.printStackTrace();
        }
    }

    private static void print(final String result) {
        final String message = String.format("Validation result:\n%s", result);
        System.out.println(message);
    }
}
