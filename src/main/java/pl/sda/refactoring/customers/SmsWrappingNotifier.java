package pl.sda.refactoring.customers;

final class SmsWrappingNotifier extends WrappingNotifier {

    protected SmsWrappingNotifier(Notifier nextNotifier) {
        super(nextNotifier);
    }

    @Override
    public void notify(CustomerRegistrationNotification notification) {
        System.out.println("sending sms");
        super.notify(notification);
    }
}
