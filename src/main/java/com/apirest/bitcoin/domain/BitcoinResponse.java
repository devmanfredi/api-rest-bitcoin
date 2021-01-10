package com.apirest.bitcoin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BitcoinResponse {
    private String base;
    private String currency;
    private String amount;
}
