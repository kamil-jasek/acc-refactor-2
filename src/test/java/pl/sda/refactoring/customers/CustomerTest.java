package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import pl.sda.refactoring.customers.exception.InvalidCustomerDataException;

final class CustomerTest {

    @Test
    public void shouldNotInitializeCompanyWithInvalidEmail() {
        assertThrows(InvalidCustomerDataException.class, () -> new Customer("invalid@", "Test S.A.", "9302030403"));
    }

    @Test
    public void shouldNotInitializeCompanyWithInvalidName() {
        assertThrows(InvalidCustomerDataException.class, () -> new Customer("test@ok.com", "F&", "9302030403"));

    }

    @Test
    public void shouldNotInitializeCompanyWithInvalidVat() {
        assertThrows(InvalidCustomerDataException.class, () -> new Customer("test@ok.com", "TestOK", "AB02030403"));
    }
}
