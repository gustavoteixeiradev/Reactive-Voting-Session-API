package dev.gustavoteixeira.votingsession.service;

import dev.gustavoteixeira.votingsession.dto.AgendaRequestDto;
import reactor.core.publisher.Mono;

public interface AgendaService {

    Mono<String> createAgenda(Mono<AgendaRequestDto> agendaRequest);

}
