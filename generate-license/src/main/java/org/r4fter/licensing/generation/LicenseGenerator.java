package org.r4fter.licensing.generation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

public class LicenseGenerator {

    private static final String PRIVATE_KEY_END_KEY = "-----END PRIVATE KEY-----";
    private static final String PRIVATE_KEY_START_KEY = "-----BEGIN PRIVATE KEY-----";

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private final String additionalLicenseInformation = "Any information that should go into the signed license part.";

    private final KeyFactory keyFactory;

    public LicenseGenerator() throws CannotGenerateLicenseException {
        try {
            this.keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            final String message = String.format("Key algorithm %s not found!", KEY_ALGORITHM);
            throw new CannotGenerateLicenseException(message, e);
        }
    }

    public String provide(final String userName) throws CannotGenerateLicenseException {
        final String privateKey = getPrivateKeyContent();
        final String privateKeyOneLine = stripKey(privateKey);

        return signInputBySHA256WithRSA(userName + additionalLicenseInformation, privateKeyOneLine);
    }

    private String signInputBySHA256WithRSA(final String input, final String privateKey) throws CannotGenerateLicenseException {
        try {
            final byte[] plainPrivateKey = Base64.getDecoder().decode(privateKey);
            final PKCS8EncodedKeySpec pcks8Spec = new PKCS8EncodedKeySpec(plainPrivateKey);

            final Signature privateSignature = Signature.getInstance(SIGNATURE_ALGORITHM);
            privateSignature.initSign(keyFactory.generatePrivate(pcks8Spec));
            privateSignature.update(input.getBytes(StandardCharsets.UTF_8));

            final byte[] signedInput = privateSignature.sign();
            return Base64.getEncoder().encodeToString(signedInput);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException e) {
            throw new CannotGenerateLicenseException("Cannot sign the input data.", e);
        }

    }

    private String stripKey(final String privateKey) {
        return privateKey
                .replaceAll("\n", "")
                .replaceAll(PRIVATE_KEY_START_KEY, "")
                .replaceAll(PRIVATE_KEY_END_KEY, "")
                ;
    }

    private String getPrivateKeyContent() throws CannotGenerateLicenseException {
        try (InputStream resourceAsStream = LicenseGenerator.class.getResourceAsStream("/privateKey_pcks8.pem")) {
            return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CannotGenerateLicenseException("Private key in PCKS8 format not found!", e);
        }
    }

}
