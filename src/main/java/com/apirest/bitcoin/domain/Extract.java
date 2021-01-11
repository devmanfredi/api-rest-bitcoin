package com.apirest.bitcoin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Extract {
    private LocalDateTime dateOfTransaction;
    private BigDecimal quantityOfBitcoin;
    private BigDecimal priceInDay;
    private BigDecimal valueOfOperation;
}
