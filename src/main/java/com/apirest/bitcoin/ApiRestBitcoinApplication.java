package com.apirest.bitcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class ApiRestBitcoinApplication {
    static {
        BlockHound.install();
    }
    public static void main(String[] args) {
        SpringApplication.run(ApiRestBitcoinApplication.class, args);
    }

}
