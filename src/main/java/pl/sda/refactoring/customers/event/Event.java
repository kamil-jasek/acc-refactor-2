package pl.sda.refactoring.customers.event;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Event {

    private final String type;
    private final LocalDateTime time;
    private final UserContext userContext;
    private final String traceId;

    protected Event(String type, UserContext userContext, String traceId) {
        this.type = requireNonNull(type);
        this.time = LocalDateTime.now();
        this.userContext = requireNonNull(userContext);
        this.traceId = requireNonNull(traceId);
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public String getTraceId() {
        return traceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return type.equals(event.type) && time.equals(event.time) && userContext.equals(event.userContext) && traceId
            .equals(event.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, time, userContext, traceId);
    }
}
