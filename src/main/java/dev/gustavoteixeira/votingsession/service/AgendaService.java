package dev.gustavoteixeira.votingsession.service;

import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.response.AgendaResponseDto;
import reactor.core.publisher.Mono;

public interface AgendaService {

    Mono<String> createAgenda(Mono<AgendaRequestDto> agendaRequest);

    Mono<AgendaResponseDto> getAgenda(String agendaId);

    Mono<Void> startAgenda(String agendaId);

}
