package com.apirest.bitcoin.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class BitPayBitcoinRequestDTO {
    private BigDecimal quantity;
}
