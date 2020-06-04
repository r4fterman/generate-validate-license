package org.r4fter.licensing.validation;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class LicenseValidator {

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private final String additionalLicenseInformation = "Any information that should go into the signed license part.";
    private final KeyFactory keyFactory;

    public LicenseValidator() throws CannotValidateLicenseException {
        try {
            this.keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            final String message = String.format("Key algorithm %s not found!", KEY_ALGORITHM);
            throw new CannotValidateLicenseException(message, e);
        }
    }

    public boolean validate(final String userName, final String signedLicense, final String publicKey) throws CannotValidateLicenseException {
        final String licenseInformation = userName + additionalLicenseInformation;

        return verify(licenseInformation, publicKey, signedLicense);
    }

    private boolean verify(final String licenseInformation, final String publicKey, final String signatureToVerify) throws CannotValidateLicenseException {
        try {
            final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(convertToPublicKey(publicKey));
            signature.update(licenseInformation.getBytes(StandardCharsets.UTF_8));

            return signature.verify(Base64.getDecoder().decode(signatureToVerify));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new CannotValidateLicenseException("Cannot validate the input data.", e);
        }
    }

    private PublicKey convertToPublicKey(final String publicKey) throws CannotValidateLicenseException {
        try {
            final byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
            final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new CannotValidateLicenseException("Cannot read public key.", e);
        }
    }

}
