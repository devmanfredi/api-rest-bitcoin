package com.apirest.bitcoin.service;

import com.apirest.bitcoin.api.BitcoinApi;
import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.dto.BitBalanceRequestDTO;
import com.apirest.bitcoin.exception.MessageException;
import com.apirest.bitcoin.repository.CustomerRepository;
import com.apirest.bitcoin.validation.DocumentValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final WebClient webClient;


    public Mono<Customer> save(Customer customer) {
        return Mono.just(customer)
                .filterWhen(cust -> isDocumentIsValid(cust.getDocument()).map(exist -> exist))
                .switchIfEmpty(Mono.error(new MessageException("CPF is not valid")))
                .flatMap(custToExisting -> customerRepository.findByDocument(customer.getDocument()))
                .flatMap(existingDocument -> Mono.error(new MessageException("CPF Already Exists")))
                .then(customerRepository.save(customer));
    }

    public Flux<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Mono<Customer> findById(String customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Mono<Customer> update(String customerId, BitBalanceRequestDTO customer) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new MessageException("Custumer not found")))
                .flatMap(cust -> {
                    cust.setBalanceReal(customer.getBalanceReal());
                    return customerRepository.save(cust);
                });
    }

    public Mono<Void> delete(String customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new MessageException("Custumer not found")))
                .flatMap(customerRepository::delete);
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    private Mono<Boolean> isDocumentIsValid(String document) {
        return Mono.just(DocumentValidation.cpfValido(document));
    }

    public Mono<Customer> startOperation(String customerId, BigDecimal quantity) {
        BitcoinApi api = new BitcoinApi();
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new MessageException("Customer not found")))
                .map(customer -> customer)
                .flatMap(customer -> api.getPrice(webClient).map(data -> new BigDecimal(data.getAmount()))
                        .flatMap(price -> {
                            if (customer.getBalanceReal().compareTo(price.multiply(quantity)) < 0) {
                                return Mono.error(new MessageException("Sem Saldo"));
                            } else {

                                customer.setBalanceBitcoin(Objects.isNull(customer.getBalanceBitcoin()) ?
                                        quantity : customer.getBalanceBitcoin().add(quantity));
                                customer.setBalanceReal(customer.getBalanceReal().subtract(price.multiply(quantity)));
                            }
                            return Mono.just(customer);
                        }))
                .flatMap(customerRepository::save);
    }
}

