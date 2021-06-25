package pl.sda.refactoring.customers.exception;

final public class InvalidCustomerDataException extends DomainException {

    public InvalidCustomerDataException(String message) {
        super(message);
    }

    public InvalidCustomerDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
