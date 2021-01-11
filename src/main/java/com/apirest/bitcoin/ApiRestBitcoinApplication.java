package com.apirest.bitcoin;

import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.repository.CustomerRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "APIs", version = "1.0", description = "Documentation APIs v1.0"))
public class ApiRestBitcoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiRestBitcoinApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ReactiveMongoOperations operations,
                           CustomerRepository customerRepository) {
        return args -> {
            Flux<Customer> customerFlux = Flux.just(
                    new Customer(null, "Heuler Manfredi", "02205652281", BigDecimal.ZERO, BigDecimal.ZERO),
                    new Customer(null, "customer2", "02205652282", BigDecimal.ZERO, BigDecimal.ZERO),
                    new Customer(null, "customer3", "02205652283", BigDecimal.ZERO, BigDecimal.ZERO),
                    new Customer(null, "customer4", "02205652284", BigDecimal.ZERO, BigDecimal.ZERO))
                    .flatMap(customerRepository::save);

            customerFlux
                    .thenMany(customerRepository.findAll())
                    .subscribe(System.out::println);

        };
    }

}
