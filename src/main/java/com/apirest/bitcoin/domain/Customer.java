package com.apirest.bitcoin.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class Customer {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String document;

    private BigDecimal balanceReal;

    private BigDecimal balanceBitcoin;
}
