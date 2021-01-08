package com.apirest.bitcoin.service;

import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.exception.MessageException;
import com.apirest.bitcoin.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Mono<Customer> save(Customer customer) throws MessageException {
        return customerRepository.findByDocument(customer.getDocument())
                .flatMap(existingDocument -> Mono.error(new MessageException("Cpf já existe")))
                .then(customerRepository.save(customer));
    }

    public Flux<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Mono<Customer> findById(String customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    public Mono<Customer> update(Customer customer) {
        return findById(customer.getId())
                .flatMap(cust -> {
                    cust.setBalanceReal(customer.getBalanceReal());
                    return customerRepository.save(cust);
                })
                .map(update -> customer);
    }

    public Mono<Void> delete(String customerId) {
        return findById(customerId)
                .flatMap(customerRepository::delete);
    }

}

