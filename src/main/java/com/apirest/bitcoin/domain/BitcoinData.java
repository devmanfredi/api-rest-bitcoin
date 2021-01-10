package com.apirest.bitcoin.domain;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class BitcoinData {
    private BitcoinResponse data;
}
