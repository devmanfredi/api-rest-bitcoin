package com.apirest.bitcoin.api;

import com.apirest.bitcoin.domain.BitcoinData;
import com.apirest.bitcoin.domain.BitcoinResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BitcoinApi {

    private WebClient client;

    public Mono<BitcoinResponse> getPrice(WebClient client) {
        this.client = client;
        return client
                .get()
                .uri("")
                .retrieve()
                .bodyToMono(BitcoinData.class)
                .map(BitcoinData::getData);
    }

}
