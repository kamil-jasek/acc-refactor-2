package pl.sda.refactoring.customers;

public final class Address {

    private final String street;
    private final String city;
    private final String zipCode;
    private final String countryCode;

    public Address(String street, String city, String zipCode, String countryCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.countryCode = countryCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}