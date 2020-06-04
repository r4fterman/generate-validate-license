package org.r4fter.licensing;

import org.r4fter.licensing.generation.CannotGenerateLicenseException;
import org.r4fter.licensing.generation.LicenseGenerator;

public class LicenseApplication {

    public static void main(final String[] args) {
        final String userName = "user@email.com";

        try {
            final LicenseGenerator licenseGenerator = new LicenseGenerator();
            final String licenseSignature = licenseGenerator.provide(userName);

            print(userName + "\n" + licenseSignature);
        } catch (CannotGenerateLicenseException e) {
            e.printStackTrace();
        }
    }

    private static void print(final String licenseSignature) {
        final String message = String.format("License signature:\n%s", licenseSignature);
        System.out.println(message);
    }
}
