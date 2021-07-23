package pl.sda.refactoring.customers.event;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

public final class UserContext {

    public enum Type {
        USER, ADMIN, SYSTEM;
    }

    private final Type type;
    private final String name;

    public UserContext(Type type, String name) {
        this.type = requireNonNull(type);
        this.name = requireNonNull(name);
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserContext that = (UserContext) o;
        return type == that.type && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }
}
