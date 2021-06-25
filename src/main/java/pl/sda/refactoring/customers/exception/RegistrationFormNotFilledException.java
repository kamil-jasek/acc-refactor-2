package pl.sda.refactoring.customers.exception;

final public class RegistrationFormNotFilledException extends DomainException {

    public RegistrationFormNotFilledException(String message) {
        super(message);
    }

    public RegistrationFormNotFilledException(String message, Throwable cause) {
        super(message, cause);
    }
}
