package org.r4fter.licensing.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LicenseValidatorTest {

    private static final String PUBLIC_KEY_START_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_END_KEY = "-----END PUBLIC KEY-----";

    private static final String CORRECT_LICENSE_SIGNATURE = "dshs3+naPicdhdEcZqKvyzXCWA63ZeWsoxVk1o2GEfSRqBYRLyhOXoErP5ekpd6jy34Dles3VJwUhoEL3ay1JEoobYHGTSRcO3EYSJCOHzi3qAtrXDQq1KsbZK9y5v+SazPYaO9+bh984bBSvOxwqE3+auLzlOfiGE8zYnuHUOah6ralYo6pCetTylE/6kVsfbxU93T2eEyEONbTjqrRzBY6Z4mDl2ld6QRzQXCisg7XIsLKAU6OHi7JObX3sZ5lTafE8xM3jBNjmTER/XvQNTjVwVZ03QD65fvZYo3VvsRIWPB+wsjz7NsuTewttfRCJbvTQL8XHPdR4pw5FV5olordlW3wQp3twxBDrntdaVnHSaSgyqG+m0BAm9HqjAGnv8RYW8wggngEMPwuybyogO3/lVhlO4F7uth+gFJzv+oMMfrwHjzmqcsIDkmrzK17Ze9iOe0aa+iCafy8KobVcz9c6ZpiOgp0aq3AMtXURq9RKuSJLzyvqq1qt6TWX7ieGd9kuT+3xWGTv34GUx40RRLNAPcaWCiw8PIUC7ayhDDg+jX95nAMWG5KfRQ1jMYJZ+mfAWNf2DBWu0iiVqjrl4zEiOCbAsqeHrAIPzzV1IEIbuL5i1yzDuoM5Jsqr6QB5GGEUOV1nLCsIwilkV40PzIjx6+RpIIA6Lxg5Pg7nk0=";
    private static final String INCORRECT_LICENSE_SIGNATURE = "bwHWHal0dcf1rszEN3UbJ2Pg4w4aTu9lG+zJDAMc08e9SwVBv6vfWHVhOP+OuKgNt/iYA2DIknF9I9PNDJxtd39aL0N8KZjRpYsB1rVXuo/36FbT15Y7a1snzsyerhp3SXvZg8d4MF/DaOvgLxZRddRrMT4ZbwKbuIRYxXPTbd2f+eNHj84zX4ZC7Bs9E2Xwiihp1vAyuvI4Cvt+iO+c43jz/C6A6+dcjWvaErBQAMkRwtYtMPDmvX9UH3jjOQGacbgYQF4yTpn7Wbw4dGpkGdi+m4FGnorgSuIweaZKBVKNZIrO99op+j/8cuv2zLfzdaAmeAVqGdivnHk2RmW9DP8hkOOGO1MXAYhtHlwbvhTVYQhI3hbPoB0QzhbusHtcQUemvy1sj6G38Xp7vKO4rXHgWyHOUFcyVocnaMsBfrB8J3UygS/9ifO0+MbQIOg42zOvQg7Iyl06C50BDAAN9ujE1qtyilBjuv7/L1G23IcJvndf4fV1zC4GcgF9wVv5F0+N9c2H2M6FXb4oe64PY/iqEwpFrgyl3V8QPVq3uYCH6gds16KudnyRYZKr9UHWsFEq5ka/EL1ynJi/mF5Z9x6lVuqCpqbWPh5hR1bXh4jhosU3NrBbgDW1QPInkSC3orI4ulpfZ4aZrqxs5vTbXjK77eaCmyjx0gEovoX1HPI=";

    private static final String USER_NAME = "user@email.com";

    private LicenseValidator licenseValidator;

    @BeforeEach
    void setUp() throws Exception {
        licenseValidator = new LicenseValidator();
    }

    @Test
    void validator_should_verify_a_correct_license() throws Exception {
        final boolean valid = licenseValidator.validate(USER_NAME, CORRECT_LICENSE_SIGNATURE, getPublicKey());

        assertThat(valid, is(true));
    }

    @Test
    void validator_should_deny_an_incorrect_license() throws Exception {
        final boolean valid = licenseValidator.validate(USER_NAME, INCORRECT_LICENSE_SIGNATURE, getPublicKey());

        assertThat(valid, is(false));
    }

    private String getPublicKey() throws IOException {
        final String publicKeyContent = getPublicKeyContent();
        return stripKey(publicKeyContent);
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