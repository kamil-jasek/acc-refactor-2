package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;
import pl.sda.refactoring.customers.exception.InvalidCustomerDataException;
import pl.sda.refactoring.customers.exception.RegistrationFormNotFilledException;

public class CustomerServiceTest {

    private final MailSender mailSender = mock(MailSender.class);

    @Test
    public void shouldRegisterVerifiedCompany() {
        // given
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var customerCapture = ArgumentCaptor.forClass(Customer.class);
        var service = new CustomerService(dao, mailSender);

        // when
        service.registerCompany(new RegisterCompanyForm("test@test.com", "Test S.A.", "9302030403", true));

        // then
        verify(dao).save(customerCapture.capture());
        var customer = customerCapture.getValue();
        assertNotNull(customer.getId());
        assertEquals(customer.getEmail(), "test@test.com");
        assertEquals(customer.getCompName(), "Test S.A.");
        assertEquals(customer.getCompVat(), "9302030403");
        assertTrue(customer.isVerf());
        assertEquals(customer.getVerifBy(), CustomerVerifier.AUTO_EMAIL);
        assertNotNull(customer.getVerfTime());
    }

    @Test
    public void shouldNotRegisterCompanyIfAlreadyExistsInDatabase() {
        // given
        final var customerDao = mock(CustomerDao.class);
        when(customerDao.emailExists(anyString())).thenReturn(true);
        when(customerDao.vatExists(anyString())).thenReturn(true);
        final var service = new CustomerService(customerDao, mailSender);

        // then
        assertThrows(CompanyAlreadyExistsException.class, () -> service.registerCompany(new RegisterCompanyForm(
            "mail@comp.com",
            "Test S.A.",
            "9302030403",
            true)));
    }

    @Test
    public void testEmailFail() {
        // given
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var service = new CustomerService(dao, mailSender);

        // then
        assertThrows(InvalidCustomerDataException.class, () -> service.registerCompany(new RegisterCompanyForm(
            "invalid@", "Test S.A.", "9302030403", true)));
    }

    @Test
    public void testNameFail() {
        // given
        final var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        final var service = new CustomerService(dao, mailSender);

        // when
        assertThrows(InvalidCustomerDataException.class, () -> service.registerCompany(new RegisterCompanyForm(
            "test@ok.com", "F&", "9302030403", true)));

    }

    @Test
    public void testVatFail() {
        // given
        final var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        final var service = new CustomerService(dao, mailSender);

        // then
        assertThrows(InvalidCustomerDataException.class, () -> service.registerCompany(new RegisterCompanyForm(
            "test@ok.com", "TestOK", "AB02030403", true)));
    }
}
