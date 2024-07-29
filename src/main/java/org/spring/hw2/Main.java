package org.spring.hw2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Tablo tablo = context.getBean(Tablo.class);

        Ticket ticket1 = tablo.newTicket();
        System.out.println("Ticket 1: " + ticket1.getNumber() + " created at " + ticket1.getCreatedAt());

        Ticket ticket2 = tablo.newTicket();
        System.out.println("Ticket 2: " + ticket2.getNumber() + " created at " + ticket2.getCreatedAt());
    }
}

