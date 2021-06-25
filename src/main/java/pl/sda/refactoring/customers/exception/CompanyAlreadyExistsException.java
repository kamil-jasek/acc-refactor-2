package pl.sda.refactoring.customers.exception;

final public class CompanyAlreadyExistsException extends DomainException {

    public CompanyAlreadyExistsException(String message) {
        super(message);
    }

    public CompanyAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
