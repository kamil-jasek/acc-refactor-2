package pl.sda.refactoring.customers;

import java.util.UUID;

final class CustomerRegistrationNotification {

    private final UUID id;
    private final String email;
    private final String name;
    private final boolean verified;

    CustomerRegistrationNotification(UUID id, String email, String name, boolean verified) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.verified = verified;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isVerified() {
        return verified;
    }
}
