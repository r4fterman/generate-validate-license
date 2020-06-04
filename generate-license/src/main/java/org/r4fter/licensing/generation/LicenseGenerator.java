package org.r4fter.licensing.generation;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class LicenseGenerator {

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

    public String provide(final String userName, final String privateKeyOneLine) throws CannotGenerateLicenseException {
        final String licenseInformation = userName + additionalLicenseInformation;

        return signInputBySHA256WithRSA(licenseInformation, privateKeyOneLine);
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

}
