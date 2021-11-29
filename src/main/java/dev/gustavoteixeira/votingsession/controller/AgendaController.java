package dev.gustavoteixeira.votingsession.controller;

import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.request.VoteRequestDto;
import dev.gustavoteixeira.votingsession.dto.response.AgendaResponseDto;
import dev.gustavoteixeira.votingsession.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
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
        return service
                .createAgenda(agendaRequest)
                .map(id -> responseCreated(id, serverRequest));
    }

    @GetMapping("/{agendaId}")
    public Mono<ResponseEntity<AgendaResponseDto>> getAgenda(@PathVariable String agendaId) {
        return service
                .getAgenda(agendaId)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{agendaId}/start")
    public Mono<ResponseEntity<Void>> startAgenda(@PathVariable String agendaId) {
        return service
                .startAgenda(agendaId)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{agendaId}/vote")
    public Mono<ResponseEntity<Void>> voteAgenda(@PathVariable String agendaId,
                                                 @RequestBody final Mono<VoteRequestDto> voteRequest) {
        return service
                .voteAgenda(agendaId, voteRequest)
                .map(ResponseEntity::ok);
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
