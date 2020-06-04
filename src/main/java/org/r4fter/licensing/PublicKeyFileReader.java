package org.r4fter.licensing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class PublicKeyFileReader {

    private static final String PRIVATE_KEY_END_KEY = "-----END PUBLIC KEY-----";
    private static final String PRIVATE_KEY_START_KEY = "-----BEGIN PUBLIC KEY-----";

    public String getPublicKeyContent(final InputStream inputStream) throws IOException {
        final String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return stripKey(content);
    }

    private String stripKey(final String privateKey) {
        return privateKey
                .replaceAll("\n", "")
                .replaceAll(PRIVATE_KEY_START_KEY, "")
                .replaceAll(PRIVATE_KEY_END_KEY, "")
                ;
    }

}
