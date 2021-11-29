package dev.gustavoteixeira.votingsession.controller;

import dev.gustavoteixeira.votingsession.dto.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.service.AgendaService;
import dev.gustavoteixeira.votingsession.service.impl.AgendaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
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

}
