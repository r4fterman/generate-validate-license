package org.r4fter.licensing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class PrivateKeyFileReader {

    private static final String PRIVATE_KEY_END_KEY = "-----END PRIVATE KEY-----";
    private static final String PRIVATE_KEY_START_KEY = "-----BEGIN PRIVATE KEY-----";

    public String getPrivateKeyContent(final InputStream inputStream) throws IOException {
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
