package pl.sda.refactoring.customers;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static pl.sda.refactoring.customers.CustomerValidator.validate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * The customer, can be person or company
 */
public class Customer {

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    // customer types
    public static final int COMPANY = 1;
    public static final int PERSON = 2;

    private UUID id;
    private int type;
    private LocalDateTime ctime;
    private String email;
    private LocalDateTime verfTime;
    private boolean verf;
    private CustomerVerifier verifBy;

    // company data
    private String compName;
    private String compVat;

    // person data
    private String fName;
    private String lName;
    private String pesel;

    private Address address;

    private Customer() {
        this.id = UUID.randomUUID();
        this.type = PERSON;
        this.ctime = LocalDateTime.now();
    }

    public Customer(String email, String fName, String lName, String pesel) {
        this();
        this.email = requireNonNull(email);
        this.fName = requireNonNull(fName);
        this.lName = requireNonNull(lName);
        this.pesel = requireNonNull(pesel);
        validatePerson();
    }

    static Customer createPersonFrom(RegisterPersonForm form) {
        return new Customer(form.getEmail(),
            form.getFirstName(),
            form.getLastName(),
            form.getPesel());
    }

    public Customer(String email, String compName, String compVat) {
        this();
        this.email = requireNonNull(email);
        this.compName = requireNonNull(compName);
        this.compVat = requireNonNull(compVat);
        validateCompany();
    }

    static Customer createCompanyFrom(RegisterCompanyForm form) {
        return new Customer(form.getEmail(), form.getName(), form.getVat());
    }

    void validateCompany() {
        validate(EMAIL_PATTERN.matcher(email).matches(), format("invalid email: %s", email));
        validate(compName.matches("[\\p{L}\\s\\.]{2,100}"), format("invalid company name: %s", compName));
        validate(compVat.matches("\\d{10}"), format("invalid company vat: %s", compVat));
    }

    private void validatePerson() {
        validate(EMAIL_PATTERN.matcher(email).matches(), format("invalid email: %s", email));
        validate(fName.matches("[\\p{L}\\s\\.]{2,100}"), format("invalid first name: %s", fName));
        validate(lName.matches("[\\p{L}\\s\\.]{2,100}"), format("invalid last name: %s", lName));
        validate(pesel.matches("/\\d{11}/"), format("invalid pesel: %s", pesel));
    }

    public UUID getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public LocalDateTime getCtime() {
        return ctime;
    }

    public String getCompName() {
        return compName;
    }

    public String getCompVat() {
        return compVat;
    }

    public String getPesel() {
        return pesel;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getVerfTime() {
        return verfTime;
    }

    public boolean isVerf() {
        return verf;
    }

    public CustomerVerifier getVerifBy() {
        return verifBy;
    }

    void updateAddress(Address address) {
        this.address = requireNonNull(address);
    }

    public Address getAddress() {
        return address;
    }

    void markVerified() {
        this.verf = true;
        this.verfTime = LocalDateTime.now();
        this.verifBy = CustomerVerifier.AUTO_EMAIL;
    }

    boolean isValidPerson() {
        return email != null && fName != null && lName != null && pesel != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return type == customer.type && verf == customer.verf && Objects.equals(id, customer.id)
            && Objects.equals(ctime, customer.ctime) && Objects.equals(email, customer.email)
            && Objects.equals(verfTime, customer.verfTime) && verifBy == customer.verifBy && Objects
            .equals(compName, customer.compName) && Objects.equals(compVat, customer.compVat) && Objects
            .equals(fName, customer.fName) && Objects.equals(lName, customer.lName) && Objects
            .equals(pesel, customer.pesel) && Objects.equals(address.getStreet(), customer.address.getStreet()) && Objects
            .equals(address.getCity(), customer.address.getCity()) && Objects.equals(address.getZipCode(),
            customer.address.getZipCode())
            && Objects.equals(address.getCountryCode(), customer.address.getCountryCode());
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(id, type, ctime, email, verfTime, verf, verifBy, compName, compVat, fName, lName, pesel,
                address.getStreet(),
                address.getCity(), address.getZipCode(), address.getCountryCode());
    }
}
