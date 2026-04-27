package org.shieldx.oracle.integration;

import lombok.RequiredArgsConstructor;
import org.shieldx.oracle.dto.ApiResponse;
import org.shieldx.oracle.dto.PaginatedApiResponse;
import org.shieldx.oracle.dto.validator.ValidatorDataWrapper;
import org.shieldx.oracle.dto.validator.ValidatorDto;
import org.shieldx.oracle.dto.validator.ValidatorListDataWrapper;
import org.shieldx.oracle.exception.ValidatorNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ValidatorApiClient {

    private final WebClient kleverWebClient;

    public Mono<ValidatorDto> fetchValidator(String address) {
        return kleverWebClient.get()
                .uri("/v1.0/validator/{address}", address)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r ->
                        Mono.error(new ValidatorNotFoundException(address)))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ValidatorDataWrapper>>() {
                })
                .map(r -> r.getData().getValidator())
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
    }

    public Flux<ValidatorDto> fetchAllValidators() {
        return kleverWebClient.get()
                .uri(u -> u.path("/v1.0/validator/list")
                        .queryParam("limit", 100)
                        .queryParam("canDelegate", true).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginatedApiResponse<ValidatorListDataWrapper>>() {
                })
                .flatMapMany(r -> Flux.fromIterable(r.getData().getValidators()));
    }
}