package org.spring.hw2;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TicketNumberGenerator {

    public String createNewNumber() {
        return "Ticket #" + UUID.randomUUID().toString();
    }
}
