package pl.sda.refactoring.customers;

import static java.lang.String.format;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import pl.sda.refactoring.customers.exception.InvalidCustomerDataException;

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

    // address data
    private String addrStreet;
    private String addrCity;
    private String addrZipCode;
    private String addrCountryCode;

    static Customer createPersonFrom(RegisterPersonForm form) {
        final var customer = new Customer();
        customer.initializePerson(form);
        return customer;
    }

    static Customer createCompanyFrom(RegisterCompanyForm form) {
        var customer = new Customer();
        customer.initializeCompany(form);
        return customer;
    }

    void initializeCompany(RegisterCompanyForm form) {
        this.id = UUID.randomUUID();
        this.type = COMPANY;
        this.ctime = LocalDateTime.now();
        this.email = form.getEmail();
        this.compName = form.getName();
        this.compVat = form.getVat();
        validateCompany();
    }

    void validateCompany() {
        validateEmail();
        validateCompanyName();
        validateVat();
    }

    private void validateEmail() {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidCustomerDataException(format("invalid email: %s", email));
        }
    }

    private void validateCompanyName() {
        if (compName == null || !compName.matches("[\\p{L}\\s\\.]{2,100}")) {
            throw new InvalidCustomerDataException(format("invalid company name: %s", compName));
        }
    }

    private void validateVat() {
        if (compVat == null || !compVat.matches("\\d{10}")) {
            throw new InvalidCustomerDataException(format("invalid company vat: %s", compVat));
        }
    }

    void initializePerson(RegisterPersonForm form) {
        this.id = UUID.randomUUID();
        this.type = PERSON;
        this.ctime = LocalDateTime.now();

        if (form.isEmailValid()) {
            this.email = form.getEmail();
        }
        if (form.isFirstNameValid()) {
            this.fName = form.getFirstName();
        }
        if (form.isLastNameValid()) {
            this.lName = form.getLastName();
        }
        if (form.isPeselValid()) {
            this.pesel = form.getPesel();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getCtime() {
        return ctime;
    }

    public void setCreateTime(LocalDateTime ctime) {
        this.ctime = ctime;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompVat() {
        return compVat;
    }

    public void setCompVat(String compVat) {
        this.compVat = compVat;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getAddrStreet() {
        return addrStreet;
    }

    public void setAddrStreet(String addrStreet) {
        this.addrStreet = addrStreet;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrZipCode() {
        return addrZipCode;
    }

    public void setAddrZipCode(String addrZipCode) {
        this.addrZipCode = addrZipCode;
    }

    public String getAddrCountryCode() {
        return addrCountryCode;
    }

    public void setAddrCountryCode(String addrCountryCode) {
        this.addrCountryCode = addrCountryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getVerfTime() {
        return verfTime;
    }

    public void setVerfTime(LocalDateTime verfTime) {
        this.verfTime = verfTime;
    }

    public boolean isVerf() {
        return verf;
    }

    public void setVerf(boolean verf) {
        this.verf = verf;
    }

    public CustomerVerifier getVerifBy() {
        return verifBy;
    }

    public void setVerifBy(CustomerVerifier verifBy) {
        this.verifBy = verifBy;
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
            .equals(pesel, customer.pesel) && Objects.equals(addrStreet, customer.addrStreet) && Objects
            .equals(addrCity, customer.addrCity) && Objects.equals(addrZipCode, customer.addrZipCode)
            && Objects.equals(addrCountryCode, customer.addrCountryCode);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(id, type, ctime, email, verfTime, verf, verifBy, compName, compVat, fName, lName, pesel, addrStreet,
                addrCity, addrZipCode, addrCountryCode);
    }
}
