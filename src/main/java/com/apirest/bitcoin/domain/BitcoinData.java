package com.apirest.bitcoin.domain;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class BitcoinData {
    private DataBit data;
}

@Data
class DataBit {
    public String base;
    public String currency;
    public String amount;
}
