package org.spring.hw2;

import java.time.LocalDateTime;

public class Ticket {

    private String number;
    private LocalDateTime createdAt;
    // Любые другие поля

    public Ticket(String number, LocalDateTime createdAt) {
        this.number = number;
        this.createdAt = createdAt;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}