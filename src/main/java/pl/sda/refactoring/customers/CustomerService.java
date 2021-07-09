package pl.sda.refactoring.customers;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;

public final class CustomerService {

    private CustomerDao dao;
    private MailSender mailSender;

    // TODO - remove and make dao final
    public CustomerService() {
    }

    public CustomerService(CustomerDao dao, MailSender mailSender) {
        this.dao = requireNonNull(dao);
        this.mailSender = requireNonNull(mailSender);
    }

    public boolean registerPerson(RegisterPersonForm form) {
        if (!form.isFilled() || isPersonRegistered(form)) {
            return false;
        }
        final Customer customer = Customer.createPersonFrom(form);
        if (!customer.isValidPerson()) {
            return false;
        }

        String subj;
        String body;
        if (form.isVerified()) {
            customer.markVerified();
            subj = "Your are now verified customer!";
            body = "<b>Hi " + form.getFirstName() + "</b><br/>" +
                "Thank you for registering in our service. Now you are verified customer!";
        } else {
            subj = "Waiting for verification";
            body = "<b>Hi " + form.getFirstName() + "</b><br/>" +
                "We registered you in our service. Please wait for verification!";
        }
        dao.save(customer);
        mailSender.sendEmail(form.getEmail(), subj, body);
        return true;
    }

    private boolean isPersonRegistered(RegisterPersonForm form) {
        return dao.emailExists(form.getEmail()) || dao.peselExists(form.getPesel());
    }

    public RegisteredCompany registerCompany(RegisterCompanyForm form) {
        if (isCompanyRegistered(form.getEmail(), form.getVat())) {
            throw new CompanyAlreadyExistsException(format("company exists: %s", form));
        }
        final var customer = Customer.createCompanyFrom(form);
        String subj;
        String body;
        if (form.isVerified()) {
            customer.markVerified();
            subj = "Your are now verified customer!";
            body = "<b>Your company: " + form.getName() + " is ready to make na order.</b><br/>" +
                "Thank you for registering in our service. Now you are verified customer!";
        } else {
            subj = "Waiting for verification";
            body = "<b>Hello</b><br/>" +
                "We registered your company: " + form.getName() + " in our service. Please wait for verification!";
        }

        dao.save(customer);
        mailSender.sendEmail(form.getEmail(), subj, body);
        return new RegisteredCompany(customer.getId());
    }

    private boolean isCompanyRegistered(String email, String vat) {
        return dao.emailExists(email) || dao.vatExists(vat);
    }

    public boolean updateAddress(UpdateCustomerAddressRequest updateRequest) {
        var result = false;
        var optional = dao.findById(updateRequest.getCustomerId());
        if (optional.isPresent()) {
           final var customer = optional.get();
           customer.updateAddress(new Address(updateRequest.getStreet(),
               updateRequest.getCity(),
               updateRequest.getZipcode(),
               updateRequest.getCountry()));
           dao.save(customer);
           result = true;
        }
        return result;
    }

    public void setDao(CustomerDao dao) {
        this.dao = dao;
    }
}
