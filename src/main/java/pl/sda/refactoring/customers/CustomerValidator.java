package pl.sda.refactoring.customers;

import pl.sda.refactoring.customers.exception.InvalidCustomerDataException;

final class CustomerValidator {

    static void validate(boolean result, String message) {
        if (!result) {
            throw new InvalidCustomerDataException(message);
        }
    }
}
