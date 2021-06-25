package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import javax.mail.Transport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class CustomerServiceTest {

    private final MailSender mailSender = mock(MailSender.class);

    @BeforeEach
    public void beforeEach() {
        when(mailSender.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
    }

    @Test
    public void testRegisterCompanyVerified() {
        // given
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var customerCapture = ArgumentCaptor.forClass(Customer.class);
        var service = new CustomerService(dao, mailSender);

        // when
        var reg = service.registerCompany(new RegisterCompanyForm("test@test.com", "Test S.A.", "9302030403", true));

        // then
        assertTrue(reg);
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
    public void testCompanyExists() {
        // given
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(true);
        when(dao.vatExists(anyString())).thenReturn(true);
        var service = new CustomerService(dao, mailSender);

        // when
        var reg = service.registerCompany(new RegisterCompanyForm("mail@comp.com", "Test S.A.", "9302030403", true));

        // then
        assertFalse(reg);
    }

    @Test
    public void testEmailFail() {
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var service = new CustomerService(dao, mailSender);
        var reg = service.registerCompany(new RegisterCompanyForm("invalid@", "Test S.A.", "9302030403", true));
        assertFalse(reg);
    }

    @Test
    public void testNameFail() {
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var service = new CustomerService(dao, mailSender);
        var reg = service.registerCompany(new RegisterCompanyForm("test@ok.com", "F&", "9302030403", true));
        assertFalse(reg);
    }

    @Test
    public void testVatFail() {
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var service = new CustomerService(dao, mailSender);
        var reg = service.registerCompany(new RegisterCompanyForm("test@ok.com", "TestOK", "AB02030403", true));
        assertFalse(reg);
    }
}
