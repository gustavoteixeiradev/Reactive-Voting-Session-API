package dev.gustavoteixeira.votingsession.service.impl;

import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.response.AgendaResponseDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;
import dev.gustavoteixeira.votingsession.exception.CreatingAgendaException;
import dev.gustavoteixeira.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.votingsession.service.AgendaService;
import dev.gustavoteixeira.votingsession.util.AgendaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaRepository repository;

    public Mono<String> createAgenda(Mono<AgendaRequestDto> agendaRequest) {
        log.info("AgendaServiceImpl.createAgenda - Start.");
        return agendaRequest
                .map(AgendaUtils::toEntity)
                .flatMap(repository::insert)
                .onErrorResume(e -> Mono.error(new CreatingAgendaException(e.getMessage())))
                .map(Agenda::getId);
    }

    @Override
    public Mono<AgendaResponseDto> getAgenda(String agendaId) {
        log.info("AgendaServiceImpl.getAgenda - Start - AgendaId: {};", agendaId);
        return repository
                .findById(agendaId)
                .map(AgendaUtils::toResponseDto);
    }

    @Override
    public Mono<Void> startAgenda(String agendaId) {
        log.info("AgendaServiceImpl.startAgenda - Start - AgendaId: {};", agendaId);
        return repository
                .findById(agendaId)
                .doOnNext(AgendaUtils::verifyIfAgendaIsAlreadyOpen)
                .doOnNext(AgendaUtils::verifyIfAgendaHasAlreadyBeenClosed)
                .doOnNext(agenda -> agenda.setStartTime(LocalDateTime.now()))
                .flatMap(repository::save)
                .then();
    }

}
