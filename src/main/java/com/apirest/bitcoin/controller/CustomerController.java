package com.apirest.bitcoin.controller;

import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}