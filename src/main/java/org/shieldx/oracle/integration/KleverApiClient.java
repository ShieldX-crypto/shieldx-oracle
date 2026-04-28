package org.shieldx.oracle.integration;

import lombok.RequiredArgsConstructor;
import org.shieldx.oracle.exception.ValidatorNotFoundException;
import org.shieldx.oracle.integration.dto.ApiResponse;
import org.shieldx.oracle.integration.dto.PaginatedApiResponse;
import org.shieldx.oracle.integration.dto.transaction.TransactionDto;
import org.shieldx.oracle.integration.dto.transaction.TransactionListDataWrapper;
import org.shieldx.oracle.integration.dto.validator.ValidatorDataWrapper;
import org.shieldx.oracle.integration.dto.validator.ValidatorDto;
import org.shieldx.oracle.integration.dto.validator.ValidatorListDataWrapper;
import org.springframework.beans.factory.annotation.Value;
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
public class KleverApiClient {
    @Value("${klever.api.page-size}")
    private int pageSize;

    @Value("${klever.api.unjail-contract-type}")
    private int unjailContractType;

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
        return fetchPage(0, pageSize)
                .expand(response -> {
                    int self = response.getPagination().getSelf();
                    int totalPages = response.getPagination().getTotalPages();
                    if (self == totalPages) {
                        return Mono.empty();
                    }
                    int nextPage = response.getPagination().getNext();
                    return fetchPage(nextPage, pageSize);
                })
                .flatMap(response ->
                        Flux.fromIterable(response.getData().getValidators())
                )
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
    }

    private Mono<PaginatedApiResponse<ValidatorListDataWrapper>> fetchPage(int page, int limit) {
        return kleverWebClient.get()
                .uri(u -> u.path("/v1.0/validator/list")
                        .queryParam("limit", limit)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public Flux<TransactionDto> fetchAllUnjailsByValidator(String address) {
        return fetchTransactionPage(address, 0)
                .expand(response -> {
                    int self = response.getPagination().getSelf();
                    int totalPages = response.getPagination().getTotalPages();
                    if (self >= totalPages) {
                        return Mono.empty();
                    }
                    return fetchTransactionPage(address, response.getPagination().getNext());
                })
                .flatMap(response ->
                        Flux.fromIterable(response.getData().getTransactions())
                )
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
    }

    private Mono<PaginatedApiResponse<TransactionListDataWrapper>> fetchTransactionPage(String address, int page) {
        return kleverWebClient.get()
                .uri(u -> u.path("/v1.0/address/{address}/transactions")
                        .queryParam("type", unjailContractType)
                        .queryParam("limit", pageSize)
                        .queryParam("page", page)
                        .build(address))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r ->
                        Mono.error(new ValidatorNotFoundException(address)))
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}