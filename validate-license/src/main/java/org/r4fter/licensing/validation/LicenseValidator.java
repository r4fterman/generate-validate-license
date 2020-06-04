package org.r4fter.licensing.validation;

import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.io.IOUtils;

public class LicenseValidator {

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String PUBLIC_KEY_START_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_END_KEY = "-----END PUBLIC KEY-----";

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

    public boolean validate(final String userName, final String signedLicense) throws CannotValidateLicenseException {
        final PublicKey publicKey = getPublicKey();

        return verify(userName + additionalLicenseInformation, publicKey, signedLicense);
    }

    private boolean verify(final String data, final PublicKey publicKey, final String sign) throws CannotValidateLicenseException {
        try {
            final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new CannotValidateLicenseException("Cannot validate the input data.", e);
        }

    }

    private PublicKey getPublicKey() throws CannotValidateLicenseException {
        try {
            final String publicKeyContent = getPublicKeyContent();
            final String publicKeyOneLine = stripKey(publicKeyContent);

            final byte[] keyBytes = Base64.getDecoder().decode(publicKeyOneLine.getBytes());
            final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (InvalidKeySpecException |IOException e) {
            throw new CannotValidateLicenseException("Cannot read public key.", e);
        }

    }

    private String stripKey(String publicKey) {
        return publicKey
                .replaceAll("\n", "")
                .replaceAll(PUBLIC_KEY_START_KEY, "")
                .replaceAll(PUBLIC_KEY_END_KEY, "")
                ;
    }

    private String getPublicKeyContent() throws IOException {
        final InputStream stream = LicenseValidator.class.getResourceAsStream("/public.pem");
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }
}
