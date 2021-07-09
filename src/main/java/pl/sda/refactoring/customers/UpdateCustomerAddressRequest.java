package pl.sda.refactoring.customers;

import java.util.UUID;

public class UpdateCustomerAddressRequest {

    private final UUID customerId;
    private final String street;
    private final String zipcode;
    private final String city;
    private final String country;

    public UpdateCustomerAddressRequest(UUID customerId, String street, String zipcode, String city, String country) {
        this.customerId = customerId;
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
