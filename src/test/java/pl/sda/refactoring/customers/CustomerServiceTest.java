package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;

public class CustomerServiceTest {

    private final InMemoryCustomerDao customerDao = new InMemoryCustomerDao();
    private final CustomerService service = new CustomerService(customerDao, (address, subject, content) -> {});

    @ParameterizedTest
    @CsvSource({
        "test@test.com,Test S.A.,9384393931",
        "email@test.com,Comp S.A.,5584393931",
    })
    public void shouldRegisterVerifiedCompany(String email, String name, String vat) {
        // when
        final var registeredCompany = service.registerCompany(new RegisterCompanyForm(email,
            name,
            vat,
            true));

        // then
        final var customer = customerDao.getCustomerById(registeredCompany.getId());
        assertEquals(customer.getId(), registeredCompany.getId());
        assertEquals(customer.getEmail(), email);
        assertEquals(customer.getCompName(), name);
        assertEquals(customer.getCompVat(), vat);
        assertTrue(customer.isVerf());
        assertEquals(customer.getVerifBy(), CustomerVerifier.AUTO_EMAIL);
        assertNotNull(customer.getVerfTime());
    }

    @Test
    public void shouldNotRegisterCompanyIfAlreadyExistsInDatabase() {
        // given
        customerDao.save(customerWithEmail("mail@comp.com"));

        // then
        assertThrows(CompanyAlreadyExistsException.class, () -> service.registerCompany(new RegisterCompanyForm(
            "mail@comp.com",
            "Test S.A.",
            "9302030403",
            true)));
    }

    private Customer customerWithEmail(String email) {
        return new Customer(email, "Some name", "0393920331");
    }
}
