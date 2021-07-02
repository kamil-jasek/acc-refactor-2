package pl.sda.refactoring.customers;

import static java.lang.String.format;

import pl.sda.refactoring.customers.exception.RegistrationFormNotFilledException;

public final class RegisterCompanyForm {

    private final String email;
    private final String name;
    private final String vat;
    private final boolean verified;

    public RegisterCompanyForm(String email, String name, String vat, boolean verified) {
        this.email = email;
        this.name = name;
        this.vat = vat;
        this.verified = verified;
        verifyForm();
    }

    private void verifyForm() {
        if (!isFilled()) {
            throw new RegistrationFormNotFilledException(format("form not filled: %s", this));
        }
    }

    boolean isFilled() {
        return getEmail() != null && getName() != null && getVat() != null;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getVat() {
        return vat;
    }

    public boolean isVerified() {
        return verified;
    }

    @Override
    public String toString() {
        return "RegisterCompanyForm{" +
            "email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", vat='" + vat + '\'' +
            ", verified=" + verified +
            '}';
    }
}
