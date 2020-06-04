package org.r4fter.licensing.generation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LicenseGeneratorTest {

    private static final String USER_NAME = "me@email.de";

    private LicenseGenerator generator;

    @BeforeEach
    public void setUp() throws Exception {
        generator = new LicenseGenerator();
    }

    @Test
    void generator_should_provide_license_for_a_user_name() throws Exception {
        final String license = generator.provide(USER_NAME);

        assertThat(license.length(), is(684));
    }
}
