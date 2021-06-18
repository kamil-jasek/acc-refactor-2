package pl.sda.refactoring.customers;

import org.junit.jupiter.api.Test;

final class TestExample {

    @Test
    void test() {
        new CustomerService().registerPerson(new RegisterPersonForm("test", "test", "test", "test", false));
    }
}
