package com.laxmifinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinanceAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceAccountApplication.class, args);
    }
}
