package com.example.authservice.service;

import com.example.authservice.api.Person;
import com.example.authservice.config.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int DELAY_MILLIS = 1000;
    private final WebClient webClient;

    public Person getUserByEmail(final String id) {
        return webClient.get()
                .uri("api/email/{id}", id)
                .retrieve()
                .bodyToMono(Person.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    public Person getUserById(final long id) {
        return webClient.get()
                .uri("api/{id}", id)
                .retrieve()
                .bodyToMono(Person.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    public Person create(Person person) {
        return webClient.post()
                .uri("api")
                .bodyValue(person)
                .retrieve()
                .bodyToMono(Person.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    public Person update(long id, Person person) {
        return webClient.put()
                .uri("api/{id}", id)
                .bodyValue(person)
                .retrieve()
                .bodyToMono(Person.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    public Person updateRole(long id, Role role) {
        return webClient.put()
                .uri("api/{id}/change/{role}", id, role)
                .retrieve()
                .bodyToMono(Person.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    public void removeById(final String id) {
        webClient.delete()
                .uri("api/{id}", id)
                .retrieve()
                .bodyToMono(Person.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    public List<Person> getAll() {
        return Objects.requireNonNull(webClient.get()
                .uri("api/all")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Person>>() {
                })
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block()).stream().toList();
    }
}
