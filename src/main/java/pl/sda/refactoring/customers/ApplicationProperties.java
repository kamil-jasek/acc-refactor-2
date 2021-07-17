package pl.sda.refactoring.customers;

import java.nio.file.Path;

final class ApplicationProperties {

    private final static ApplicationProperties INSTANCE;

    static {
        INSTANCE = new ApplicationProperties();
    }

    private ApplicationProperties() {}

    public static ApplicationProperties getInstance() {
        return INSTANCE;
    }
}
