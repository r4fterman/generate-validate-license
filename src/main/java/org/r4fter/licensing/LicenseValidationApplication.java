package org.r4fter.licensing;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.r4fter.licensing.generation.CannotGenerateLicenseException;
import org.r4fter.licensing.generation.LicenseGenerator;
import org.r4fter.licensing.validation.CannotValidateLicenseException;
import org.r4fter.licensing.validation.LicenseValidator;

public class LicenseValidationApplication {

    private static final String OPTION_SIGN = "pk";
    private static final String OPTION_VERIFY = "v";
    private static final String OPTION_USER_NAME = "u";
    private static final String OPTION_SIGNATURE = "s";
    private static final String OPTION_HELP = "h";

    public static void main(final String[] args) {
        try {
            final CommandLine commandLine = parseArguments(args);

            if (commandLine.hasOption(OPTION_SIGN) || commandLine.hasOption(OPTION_VERIFY)) {
                final LicenseValidationApplication application = new LicenseValidationApplication();

                final String userName = "user@email.com";

                if (commandLine.hasOption(OPTION_SIGN)) {
                    final String privateKeyFile = commandLine.getOptionValue(OPTION_SIGN);
                    application.signLicense(userName, getPrivateKey(privateKeyFile));
                } else {
                    final String publicKeyFile = commandLine.getOptionValue(OPTION_VERIFY);
                    final String signature = commandLine.getOptionValue(OPTION_SIGNATURE);
                    application.verifyLicense(userName, getPublicKey(publicKeyFile), signature);
                }
            } else {
                showHelp();

                final int exitCode = commandLine.hasOption(OPTION_HELP) ? 0 : 1;
                System.exit(exitCode);
            }
        } catch (ParseException | CannotValidateLicenseException | CannotGenerateLicenseException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static String getPrivateKey(String privateKeyFile) {
        return null;
    }

    private static String getPublicKey(String publicKeyFile) {
        return null;
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        return parser.parse(getOptions(), args);
    }

    private static Options getOptions() {
        final Options options = new Options();

        options.addOption(OPTION_HELP, "help", false, "Show this message");
        options.addOption(OPTION_SIGN, "sign", true, "Sign text with the given private key file");
        options.addOption(OPTION_VERIFY, "verify", true, "Verify text with the given public key file");
        options.addOption(OPTION_USER_NAME, "user", true, "User name to use for signature text");
        options.addOption(OPTION_SIGNATURE, "signature", true, "Signature of a text");

        return options;
    }

    private static void showHelp() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("license application", getOptions());
    }

    private void signLicense(final String userName, final String privateKey) throws CannotGenerateLicenseException {
        final LicenseGenerator licenseGenerator = new LicenseGenerator();
        final String signature = licenseGenerator.provide(userName, privateKey);

        final String message = String.format("Signature for %s:\n%s", userName, signature);
        System.out.println(message);
    }

    private void verifyLicense(final String userName, final String publicKey, final String signature) throws CannotValidateLicenseException {
        final LicenseValidator licenseValidator = new LicenseValidator();
        final boolean valid = licenseValidator.validate(userName, signature, publicKey);

        final String message = String.format("Validation result:\n%s", userName + "\n" + valid);
        System.out.println(message);
    }

}
