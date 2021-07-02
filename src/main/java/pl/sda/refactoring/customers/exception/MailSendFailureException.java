package pl.sda.refactoring.customers.exception;

public final class MailSendFailureException extends DomainException {

    public MailSendFailureException(String message) {
        super(message);
    }

    public MailSendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
