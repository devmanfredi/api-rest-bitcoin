package com.apirest.bitcoin.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table("customer")
public class Customer {

    @Id
    private Long id;

    @NotNull
    @NotEmpty(message = "The name cannot be empty")
    private String name;

    @NotNull
    @NotEmpty(message = "The document cannot be empty")
    private String document;

    @NotNull
    private BigDecimal balance;
}
