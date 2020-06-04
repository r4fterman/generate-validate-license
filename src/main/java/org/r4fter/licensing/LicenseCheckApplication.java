package org.r4fter.licensing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.r4fter.licensing.generation.CannotGenerateLicenseException;
import org.r4fter.licensing.generation.LicenseGenerator;
import org.r4fter.licensing.validation.CannotValidateLicenseException;
import org.r4fter.licensing.validation.LicenseValidator;

public class LicenseCheckApplication {

    private static final String OPTION_SIGN = "pk";
    private static final String OPTION_VERIFY = "v";
    private static final String OPTION_USER_NAME = "u";
    private static final String OPTION_SIGNATURE = "s";
    private static final String OPTION_HELP = "h";

    public static void main(final String[] args) {
        try {
            final CommandLine commandLine = parseArguments(args);

            if (commandLine.hasOption(OPTION_SIGN) || commandLine.hasOption(OPTION_VERIFY)) {
                final LicenseCheckApplication application = new LicenseCheckApplication();

                final String userName = getMandatoryOption(commandLine, OPTION_USER_NAME);

                if (commandLine.hasOption(OPTION_SIGN)) {
                    final String privateKey = getPrivateKey(getMandatoryOption(commandLine, OPTION_SIGN));

                    application.signLicense(userName, privateKey);
                } else {
                    final String publicKeyFile = getMandatoryOption(commandLine, OPTION_VERIFY);
                    final String signature = getMandatoryOption(commandLine, OPTION_SIGNATURE);

                    application.verifyLicense(userName, getPublicKey(publicKeyFile), signature);
                }
            } else {
                showHelp();

                final int exitCode = commandLine.hasOption(OPTION_HELP) ? 0 : 1;
                System.exit(exitCode);
            }
        } catch (ParseException | CannotValidateLicenseException | CannotGenerateLicenseException | IOException | MissingMandatoryOptionValue e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static String getPrivateKey(final String privateKeyFile) throws IOException {
        final PrivateKeyFileReader reader = new PrivateKeyFileReader();
        try (FileInputStream inputStream = new FileInputStream(new File(privateKeyFile))) {
            return reader.getPrivateKeyContent(inputStream);
        }
    }

    private static String getPublicKey(final String publicKeyFile) throws IOException {
        final PublicKeyFileReader reader = new PublicKeyFileReader();
        try (FileInputStream inputStream = new FileInputStream(new File(publicKeyFile))) {
            return reader.getPublicKeyContent(inputStream);
        }
    }

    private static CommandLine parseArguments(final String[] args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        return parser.parse(getOptions(), args);
    }

    private static String getMandatoryOption(CommandLine commandLine, String option) throws MissingMandatoryOptionValue {
        if (commandLine.hasOption(option)) {
            final String value = commandLine.getOptionValue(option);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        throw new MissingMandatoryOptionValue(option);
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
        final String header = "Use the following options to sign a text or validate a signature against a text.";
        final String footer = "\n"
                + "Examples\n"
                + "For signing: license-check -pk <private key file path> -u <user name>\n"
                + "For verifying: license-check -v <public key file path> -u <user name> -s <signature>";

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("license-check", header, getOptions(), footer, true);
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

        final String message = String.format("Validation result: %s\n%s", userName, valid);
        System.out.println(message);
    }

}
