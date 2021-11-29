package dev.gustavoteixeira.votingsession.controller;

import dev.gustavoteixeira.votingsession.dto.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/agenda")
public class AgendaController {

    @Autowired
    private AgendaService service;

    @PostMapping
    public Mono<ResponseEntity<Void>> createAgenda(@RequestBody final Mono<AgendaRequestDto> agendaRequest,
                                                   ServerHttpRequest serverRequest) {
        return service.createAgenda(agendaRequest)
                .map(id -> responseCreated(id, serverRequest));
    }

    private ResponseEntity<Void> responseCreated(final String id, final ServerHttpRequest serverRequest) {
        return ResponseEntity.created(createURILocation(id, serverRequest)).build();
    }

    private URI createURILocation(final String id, final ServerHttpRequest serverRequest) {
        return UriComponentsBuilder
                .fromHttpRequest(serverRequest)
                .path("/{agendaId}")
                .buildAndExpand(id)
                .toUri();
    }

}
