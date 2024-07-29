package org.spring.hw2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Tablo {

    private final TicketNumberGenerator ticketNumberGenerator;

    @Autowired
    public Tablo(TicketNumberGenerator ticketNumberGenerator) {
        this.ticketNumberGenerator = ticketNumberGenerator;
    }

    public Ticket newTicket() {
        String ticketNumber = ticketNumberGenerator.createNewNumber();
        LocalDateTime createdAt = LocalDateTime.now();
        return new Ticket(ticketNumber, createdAt);
    }
}
