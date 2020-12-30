package com.apirest.bitcoin.controller;

import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/rest/bitcoin/customer")
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping()
    public Flux<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping
    public Mono<Customer> save(@Valid @RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @PutMapping("/{id}")
    public Mono<Customer> update(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        return customerService.update(customer.withId(id));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return customerService.delete(id);
    }
}