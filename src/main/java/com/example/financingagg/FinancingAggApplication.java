package com.example.financingagg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
public class FinancingAggApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancingAggApplication.class, args);
    }

}
