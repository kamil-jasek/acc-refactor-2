package pl.sda.refactoring.customers;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;

public final class CustomerService {

    private CustomerDao dao;
    private Notifier notifier;

    // TODO - remove and make dao final
    public CustomerService() {
    }

    public CustomerService(CustomerDao dao, Notifier notifier) {
        this.dao = requireNonNull(dao);
        this.notifier = requireNonNull(notifier);
    }

    public boolean registerPerson(RegisterPersonForm form) {
        if (!form.isFilled() || isPersonRegistered(form)) {
            return false;
        }
        final Customer customer = Customer.createPersonFrom(form);
        if (!customer.isValidPerson()) {
            return false;
        }
        if (form.isVerified()) {
            customer.markVerified();
        }
        dao.save(customer);
        notifier.notify(new CustomerRegistrationNotification(customer.getId(),
            customer.getEmail(),
            customer.getlName(),
            customer.isVerf()));
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
        if (form.isVerified()) {
            customer.markVerified();
        }

        dao.save(customer);
        notifier.notify(new CustomerRegistrationNotification(customer.getId(),
            customer.getEmail(),
            customer.getCompName(),
            customer.isVerf()));
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
