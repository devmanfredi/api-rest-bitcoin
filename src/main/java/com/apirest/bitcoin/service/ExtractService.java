package com.apirest.bitcoin.service;

import com.apirest.bitcoin.domain.BitcoinResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExtractService {

    public void save(BigDecimal quantity, Mono<BitcoinResponse> price) {

    }
}
