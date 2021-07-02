package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import pl.sda.refactoring.customers.exception.InvalidCustomerDataException;

final class CustomerTest {

    @Test
    public void shouldNotInitializeCompanyWithInvalidEmail() {
        // given
        final var customer = new Customer();

        // then
        assertThrows(InvalidCustomerDataException.class, () -> customer.initializeCompany(new RegisterCompanyForm(
            "invalid@", "Test S.A.", "9302030403", true)));
    }

    @Test
    public void shouldNotInitializeCompanyWithInvalidName() {
        // given
        final var customer = new Customer();

        // when
        assertThrows(InvalidCustomerDataException.class, () -> customer.initializeCompany(new RegisterCompanyForm(
            "test@ok.com", "F&", "9302030403", true)));

    }

    @Test
    public void shouldNotInitializeCompanyWithInvalidVat() {
        // given
        final var customer = new Customer();

        // then
        assertThrows(InvalidCustomerDataException.class, () -> customer.initializeCompany(new RegisterCompanyForm(
            "test@ok.com", "TestOK", "AB02030403", true)));
    }
}
