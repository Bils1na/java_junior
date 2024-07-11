package org.example.hw2;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Program {

    @RandomDate(min = 1704067200000L, max = 1735689600000L, zone = "UTC")
    private Date randomDate;

    @RandomDate(min = 1704067200000L, max = 1735689600000L, zone = "UTC")
    private Instant randomInstant;

    @RandomDate(min = 1704067200000L, max = 1735689600000L, zone = "UTC")
    private LocalDate randomLocalDate;

    @RandomDate(min = 1704067200000L, max = 1735689600000L, zone = "UTC")
    private LocalDateTime randomLocalDateTime;

    public static void main(String[] args) {
        Program program = new Program();
        ObjectCreator.create(program);

        System.out.println("Random Date: " + program.randomDate);
        System.out.println("Random Instant: " + program.randomInstant);
        System.out.println("Random LocalDate: " + program.randomLocalDate);
        System.out.println("Random LocalDateTime: " + program.randomLocalDateTime);
    }

}
