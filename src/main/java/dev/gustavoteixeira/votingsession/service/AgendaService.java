package dev.gustavoteixeira.votingsession.service;

import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.request.VoteRequestDto;
import dev.gustavoteixeira.votingsession.dto.response.AgendaResponseDto;
import reactor.core.publisher.Mono;

public interface AgendaService {

    Mono<String> createAgenda(Mono<AgendaRequestDto> agendaRequest);

    Mono<AgendaResponseDto> getAgenda(String agendaId);

    Mono<Void> startAgenda(String agendaId);

    Mono<Void> voteAgenda(String agendaId, Mono<VoteRequestDto> voteRequest);

}
