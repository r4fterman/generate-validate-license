package org.r4fter.licensing;

public class MissingMandatoryOptionValue extends Exception {

    public MissingMandatoryOptionValue(final String option) {
        super(String.format("The option %s is required and must be specified with an appropriate value, e.g. %s <value>", option, option));
    }
}
