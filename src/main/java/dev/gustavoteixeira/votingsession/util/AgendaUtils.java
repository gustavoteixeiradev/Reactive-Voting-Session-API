package dev.gustavoteixeira.votingsession.util;

import dev.gustavoteixeira.votingsession.constants.UserStatus;
import dev.gustavoteixeira.votingsession.dto.UserInfoDto;
import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.request.VoteRequestDto;
import dev.gustavoteixeira.votingsession.dto.response.AgendaResponseDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;
import dev.gustavoteixeira.votingsession.exception.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class AgendaUtils {

    public static final String NEGATIVE_VOTE = "Não";
    public static final String POSITIVE_VOTE = "Sim";
    public static final int DEFAULT_DURATION = 1;

    private AgendaUtils() {
    }

    public static AgendaResponseDto toResponseDto(Agenda agenda) {
        return AgendaResponseDto.builder()
                .id(agenda.getId())
                .name(agenda.getName())
                .startTime(agenda.getStartTime())
                .duration(agenda.getDuration())
                .isOpened(isOpened(agenda))
                .positiveVotes(calculateVotes(agenda, POSITIVE_VOTE))
                .negativeVotes(calculateVotes(agenda, NEGATIVE_VOTE))
                .build();
    }

    public static Agenda toEntity(AgendaRequestDto agendaDto) {
        return Agenda.builder()
                .name(agendaDto.getName())
                .duration(agendaDto.getDuration() <= 0 ? DEFAULT_DURATION : agendaDto.getDuration())
                .build();
    }

    public static boolean isOpened(Agenda agenda) {
        return agenda.getStartTime() != null && (agenda.getStartTime().plusMinutes(agenda.getDuration()).isAfter(LocalDateTime.now()));
    }

    public static void agendaMustBeOpen(Agenda agenda) {
        log.info("Validando... - Agenda deve estar aberta - Status: {}", agenda.getId());
        if (!isOpened(agenda)) {
            log.error("Erro - Agenda não está aberta - AgendaId: {}", agenda.getId());
            throw new AgendaIsNotOpenException();
        }
    }

    public static void agendaMustNotHaveBeenClosed(Agenda agenda) {
        log.info("Validando... - Agenda não deve já ter sido encerrada - Status: {}", agenda.getId());
        if (!isOpened(agenda) && agenda.getStartTime() != null) {
            log.error("Erro - Agenda já foi encerrada - AgendaId: {}", agenda.getId());
            throw new AgendaHasAlreadyBeenClosedException();
        }
    }

    public static void verifyIfAssociateIsAbleToVote(UserInfoDto userInfo) {
        log.info("Validando... - Verificando se o associado está apto a votar - User status: {}", userInfo.getStatus());
        if (userInfo.getStatus().equals(UserStatus.UNABLE_TO_VOTE)) {
            log.error("Erro - Associado não está apto a votar.");
            throw new AssociateIsNotAbleToVoteException();
        }
    }

    private static long calculateVotes(Agenda agenda, String choice) {
        return agenda.getVotes() == null ? 0 : agenda.getVotes().stream()
                .filter(vote -> vote.getChoice().equals(choice)).count();
    }


    public static void verifyIfAssociateHaveNotVotedYet(Agenda agenda, VoteRequestDto voteRequest) {
        log.info("Validando... - Verificando se o associado já não votou - AgendaId: {}, Associate: {}, Choice: {}.",
                agenda.getId(), voteRequest.getAssociate(), voteRequest.getChoice());
        if (agenda.getVotes() != null && agenda.getVotes().stream().anyMatch(vote -> vote.getAssociate().equals(voteRequest.getAssociate()))) {
            log.error("Erro - Associado já votou - Agenda identifier: {}, Associate identifier: {}", agenda.getId(), voteRequest.getAssociate());
            throw new VoteAlreadyExistsException();
        }
    }

    public static void agendaMustNotBeOpen(Agenda agenda) {
        log.info("Validando... - Agenda não deve estar aberta - Status: {}", agenda.getId());
        if (isOpened(agenda)) {
            log.error("Erro - Agenda já está aberta - AgendaId: {}", agenda.getId());
            throw new AgendaIsAlreadyOpenException();
        }
    }
}
