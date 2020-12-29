package com.apirest.bitcoin.builders;

import com.apirest.bitcoin.domain.Customer;

import java.math.BigDecimal;

public class CustomerBuilder {
    private Customer customer;

    public static CustomerBuilder createCustomerToBeSaved(){
        CustomerBuilder builder = new CustomerBuilder();
        builder.customer = Customer.builder()
                .name("Heuler")
                .document("02205652281")
                .balance(BigDecimal.valueOf(10000))
                .build();
        return builder;
    }


    public static CustomerBuilder createValidCustomer(){
        CustomerBuilder builder = new CustomerBuilder();
        builder.customer = Customer.builder()
                .id(1L)
                .name("Heuler")
                .document("02205652281")
                .balance(BigDecimal.valueOf(10000))
                .build();
        return builder;
    }

    public static CustomerBuilder createValidUpdateCustomer(){
        CustomerBuilder builder = new CustomerBuilder();
        builder.customer = Customer.builder()
                .id(1L)
                .name("Heuler Manfredi")
                .document("02205652281")
                .balance(BigDecimal.valueOf(10000))
                .build();
        return builder;
    }

    public Customer build() {
        return customer;
    }
}
