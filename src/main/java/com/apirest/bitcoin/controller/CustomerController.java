package com.apirest.bitcoin.controller;

import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.dto.BitBalanceRequestDTO;
import com.apirest.bitcoin.dto.BitPayBitcoinRequestDTO;
import com.apirest.bitcoin.exception.MessageException;
import com.apirest.bitcoin.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/rest/customer")
@Tag(name = "Customers", description = "Endpoint para gerenciamento de Clientes")
public class CustomerController {

    private final CustomerService customerService;


    @Operation(description = "Retorna uma lista de clientes")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Flux<Customer> findAll() {
        return customerService.findAll();
    }

    @Operation(description = "busca cliente pelo id", parameters = {
            @Parameter(name = "id", in = ParameterIn.QUERY, required = true, description = "id parameter")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Customer> findById(@PathVariable String id) {
        return customerService.findById(id);
    }

    @Operation(description = "Cria um novo cliente", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody())
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> save(@Valid @RequestBody Customer customer) throws MessageException {
        return customerService.save(customer);
    }

    @Operation(description = "Tranfere saldo para conta do cliente", parameters = {
            @Parameter(name = "id", in = ParameterIn.QUERY, required = true, description = "id parameter"),
            @Parameter(name = "balance_real", in = ParameterIn.HEADER, required = true, description = "valor da transferência")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Customer> update(@PathVariable String id, @Valid @RequestBody BitBalanceRequestDTO customer) {
        return customerService.update(id, customer);
    }

    @Operation(description = "deleta cliente pelo id", parameters = {
            @Parameter(name = "id", in = ParameterIn.QUERY, required = true, description = "id parameter")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return customerService.delete(id);
    }

    @Operation(description = "Compra Bitcoins", parameters = {
            @Parameter(name = "id", in = ParameterIn.QUERY, required = true, description = "id parameter"),
            @Parameter(name = "quantity", in = ParameterIn.HEADER, required = true, description = "quantidade de bitcoins")
    })
    @PostMapping("/bitcoin/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Customer> buyBitcoin(@PathVariable String customerId, @Valid @RequestBody BitPayBitcoinRequestDTO quantityBitcoin) {
        return customerService.startOperation(customerId, quantityBitcoin.getQuantity());
    }
}