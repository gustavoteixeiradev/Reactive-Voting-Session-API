package dev.gustavoteixeira.votingsession.service.impl;

import dev.gustavoteixeira.votingsession.client.UserInfoClient;
import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.request.VoteRequestDto;
import dev.gustavoteixeira.votingsession.dto.response.AgendaResponseDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;
import dev.gustavoteixeira.votingsession.entity.Vote;
import dev.gustavoteixeira.votingsession.exception.CreatingAgendaException;
import dev.gustavoteixeira.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.votingsession.service.AgendaService;
import dev.gustavoteixeira.votingsession.util.AgendaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private UserInfoClient userInfoClient;

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
                .doOnNext(AgendaUtils::agendaMustNotBeOpen)
                .doOnNext(AgendaUtils::agendaMustNotHaveBeenClosed)
                .doOnNext(agenda -> agenda.setStartTime(LocalDateTime.now()))
                .flatMap(repository::save)
                .then();
    }

    @Override
    public Mono<Void> voteAgenda(String agendaId, Mono<VoteRequestDto> voteRequest) {
        log.info("AgendaServiceImpl.voteAgenda - Start - AgendaId: {};", agendaId);
        return repository
                .findById(agendaId)
                .doOnNext(AgendaUtils::agendaMustBeOpen)
                .flatMap(agenda -> voteRequest.flatMap(vote -> userInfoClient.getUserInfo(vote.getAssociate())
                                .map(userInfoDto -> {
                                    AgendaUtils.verifyIfAssociateIsAbleToVote(userInfoDto);
                                    return vote;
                                }))
                        .map(vote -> {

                            AgendaUtils.verifyIfAssociateHaveNotVotedYet(agenda, vote);
                            List<Vote> votes = agenda.getVotes() == null ? new ArrayList<>() : agenda.getVotes();
                            votes.add(Vote.builder()
                                    .associate(vote.getAssociate())
                                    .choice(vote.getChoice())
                                    .build());
                            agenda.setVotes(votes);
                            return agenda;
                        })
                        .flatMap(repository::save))
                .then();
    }

}
