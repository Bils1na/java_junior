package org.example.hw2;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class ObjectCreator {

    public static void create(Object obj) {
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        Random random = new Random();

        for (Field field : fields) {
            if (field.isAnnotationPresent(RandomDate.class)) {
                RandomDate randomDate = field.getAnnotation(RandomDate.class);
                long min = randomDate.min();
                long max = randomDate.max();

                if (min >= max) {
                    throw new IllegalArgumentException("min must be less than max");
                }

                long randomTime = min + (long) (random.nextDouble() * (max - min));
                ZoneId zoneId = ZoneId.of(randomDate.zone());

                field.setAccessible(true);
                try {
                    if (field.getType().equals(Date.class)) {
                        field.set(obj, new Date(randomTime));
                    } else if (field.getType().equals(Instant.class)) {
                        field.set(obj, Instant.ofEpochMilli(randomTime));
                    } else if (field.getType().equals(LocalDate.class)) {
                        field.set(obj, Instant.ofEpochMilli(randomTime).atZone(zoneId).toLocalDate());
                    } else if (field.getType().equals(LocalDateTime.class)) {
                        field.set(obj, Instant.ofEpochMilli(randomTime).atZone(zoneId).toLocalDateTime());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

