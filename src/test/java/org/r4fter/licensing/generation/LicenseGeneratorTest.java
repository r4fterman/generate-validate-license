package org.r4fter.licensing.generation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.r4fter.licensing.PrivateKeyFileReader;

class LicenseGeneratorTest {

    private static final String USER_NAME = "me@email.de";

    private LicenseGenerator generator;
    private PrivateKeyFileReader reader;

    @BeforeEach
    public void setUp() throws Exception {
        generator = new LicenseGenerator();
        reader = new PrivateKeyFileReader();
    }

    @Test
    void generator_should_provide_license_for_a_user_name() throws Exception {
        final String signedLicense = generator.provide(USER_NAME, getPrivateKeyContent("/privateKey_pcks8.pem"));

        assertThat(signedLicense, is("f1LFMQDPCqSmRFyD82QFhF5r/w9hzZj1vwHFE4lDKb4sIqST+YdIvfRjaqRKERpJHFubAsOqU/BBY6Mr8+sR91dk89pzd99J4AEr3Fl1D84NGbc7JYczyjzE7N4LoOb3QHM8ZoLDvWy/X44a+9c3XMtbN7JmjBcCo114hRgBh33oLDjxsDooVHwKdFtO6z0IrjK6zhiASaK5B+9TuzoUUTSzCAQlZc4EwrJ0kf/ZqcdxyEV3d3itoCs78I2tHj70/YvHdR2eBM2cx+jj9F2F297aE+W8P3NvtjLuWtw2LaM0KEznoE7xhPyi6Ie5UdZewlG9tPoaBlEM160szDPOO1OiFM4azhPy1YnIc0RKuOqnEkSBDhIHo7cRMSuyjj/YqsLPDJw3H/h1U+cevpzYvYOId77LQzaIMRLyZijuKSgJ5HVB6JJEc7pr3m+5VjN2E9hwOG651yFHQHZQ/Z8b99Brp++DjHVo+dPomSuMA2X2WyIPAx1Hszb4+050cI/0GJH80r2+OSr5wzSmtHxDCT0sRK5OE2J0VNK6cggC4BGBHjkXfvGw62vLT6y/Lx0W0+Qk0Y1Xmc03+B0lDgzC7p1TpyFoVze/J6eYLTZb6Jpjb1n58ruLzUkwDpxhoLHKoRKhFrzMdg/EycgoFkzRa30fmnUDTZZqSM+4lUjselQ="));
    }

    private String getPrivateKeyContent(final String filePath) throws IOException {
        try (InputStream resourceAsStream = LicenseGenerator.class.getResourceAsStream(filePath)) {
            return reader.getPrivateKeyContent(resourceAsStream);
        }
    }

}
