package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import pl.sda.refactoring.customers.exception.RegistrationFormNotFilledException;

final class RegisterCompanyFormTest {

    @Test
    public void shouldNotRegisterCompanyIfFormIsNotFilled() {
        assertThrows(RegistrationFormNotFilledException.class, () -> new RegisterCompanyForm(
            null, null, null, true));
    }
}
