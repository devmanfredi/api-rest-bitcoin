package com.apirest.bitcoin.service;

import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Flux<Customer> findAll(){
        return customerRepository.findAll();
    }
}

