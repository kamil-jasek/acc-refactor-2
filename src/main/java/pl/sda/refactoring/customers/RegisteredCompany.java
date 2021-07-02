package pl.sda.refactoring.customers;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

final class RegisteredCompany {

    private final UUID customerId;

    public RegisteredCompany(UUID id) {
        customerId = requireNonNull(id);
    }

    public UUID getId() {
        return customerId;
    }
}
