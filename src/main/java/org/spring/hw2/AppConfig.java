package org.spring.hw2;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.spring") // Укажите ваш пакет здесь
public class AppConfig {
}
