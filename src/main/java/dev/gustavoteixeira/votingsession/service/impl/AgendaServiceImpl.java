package dev.gustavoteixeira.votingsession.service.impl;

import dev.gustavoteixeira.votingsession.dto.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;
import dev.gustavoteixeira.votingsession.exception.CreatingAgendaException;
import dev.gustavoteixeira.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.votingsession.service.AgendaService;
import dev.gustavoteixeira.votingsession.util.EntityDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaRepository repository;

    public Mono<String> createAgenda(Mono<AgendaRequestDto> agendaRequest) {
        log.info("AgendaServiceImpl.createAgenda - Start.");
        return agendaRequest
                .map(EntityDtoUtil::toEntity)
                .flatMap(repository::insert)
                .onErrorResume(e -> Mono.error(new CreatingAgendaException(e.getMessage())))
                .map(Agenda::getId);
    }


}
