package pl.sda.refactoring.customers;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

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
            customer.setVerf(false);
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

    /**
     * Register new company type customer
     * @param email
     * @param name
     * @param vat
     * @param verified
     * @return
     */
    public boolean registerCompany(String email, String name, String vat, boolean verified) {
        var result = false;
        var customer = new Customer();
        customer.setType(Customer.COMPANY);
        var isInDb = dao.emailExists(email) || dao.vatExists(vat);
        if (!isInDb) {
            if (email != null && name != null && vat != null) {
                var emailP = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                var emailM = emailP.matcher(email);
                if (emailM.matches()) {
                    customer.setEmail(email);
                }
                if (name.length() > 0 && name.matches("[\\p{L}\\s\\.]{2,100}")) {
                    customer.setCompName(name);
                }
                if (vat.length() == 10 && vat.matches("/\\d{10}/")) {
                    customer.setCompVat(vat);
                }

                if (customer.isValidPerson()) {
                    result = true;
                }
            }
        }

        if (result == true) {
            customer.setCreateTime(LocalDateTime.now());
            String subj;
            String body;
            if (verified) {
                customer.setVerf(verified);
                customer.setVerfTime(LocalDateTime.now());
                customer.setVerifBy(CustomerVerifier.AUTO_EMAIL);
                subj = "Your are now verified customer!";
                body = "<b>Your company: " + name + " is ready to make na order.</b><br/>" +
                    "Thank you for registering in our service. Now you are verified customer!";
            } else {
                customer.setVerf(false);
                subj = "Waiting for verification";
                body = "<b>Hello</b><br/>" +
                    "We registered your company: " + name + " in our service. Please wait for verification!";
            }
            customer.setId(UUID.randomUUID());
            dao.save(customer);
            // send email to customer
            mailSender.sendEmail(email, subj, body);
        }

        return result;
    }

    /**
     * Set new address for customer
     * @param cid
     * @param str
     * @param zipcode
     * @param city
     * @param country
     * @return
     */
    public boolean updateAddress(UUID cid, String str, String zipcode, String city, String country) {
        var result = false;
        var customer = dao.findById(cid);
        if (customer.isPresent()) {
           var object = customer.get();
           object.setAddrStreet(str);
           object.setAddrZipCode(zipcode);
           object.setAddrCity(city);
           object.setAddrCountryCode(country);
           dao.save(object);
           result = true;
        }
        return result;
    }

    public void setDao(CustomerDao dao) {
        this.dao = dao;
    }
}
