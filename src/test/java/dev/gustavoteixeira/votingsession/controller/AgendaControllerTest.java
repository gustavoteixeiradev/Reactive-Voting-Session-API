package dev.gustavoteixeira.votingsession.controller;

import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.response.AgendaResponseDto;
import dev.gustavoteixeira.votingsession.service.AgendaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(AgendaController.class)
public class AgendaControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private AgendaService service;

    @Test
    public void shouldCreateAgenda() {
        var request = Mono.just(AgendaRequestDto.builder()
                .name("A random name")
                .duration(1)
                .build());

        when(service.createAgenda(any())).thenReturn(Mono.just("1"));

        webClient.post()
                .uri("/api/v1/agenda")
                .body(Mono.just(request), AgendaRequestDto.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader().location("/api/v1/agenda/1");
    }

    @Test
    public void shouldGetAgenda() {
        LocalDateTime startedTime = LocalDateTime.now().minusMinutes(2);
        var agendaId = "1";

        when(service.getAgenda(anyString())).thenReturn(
                Mono.just(AgendaResponseDto.builder()
                        .id(agendaId)
                        .name("A random name")
                        .duration(10)
                        .startTime(startedTime)
                        .positiveVotes(2)
                        .negativeVotes(2)
                        .build())
        );

        webClient.get()
                .uri("/api/v1/agenda/{agendaId}", agendaId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AgendaResponseDto.class);
    }

    @Test
    public void shouldStartAgenda() {
        var agendaId = "1";

        when(service.startAgenda(anyString())).thenReturn(Mono.empty());

        webClient.patch()
                .uri("/api/v1/agenda/{agendaId}/start", agendaId)
                .exchange()
                .expectStatus()
                .isOk();
    }

}
