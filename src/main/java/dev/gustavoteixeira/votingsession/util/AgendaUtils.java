package dev.gustavoteixeira.votingsession.util;

import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.response.AgendaResponseDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;
import dev.gustavoteixeira.votingsession.exception.AgendaHasAlreadyBeenClosedException;
import dev.gustavoteixeira.votingsession.exception.AgendaIsAlreadyOpenException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class AgendaUtils {

    public static final String NEGATIVE_VOTE = "Não";
    public static final String POSITIVE_VOTE = "Sim";
    public static final int DEFAULT_DURATION = 1;

    public static AgendaResponseDto toResponseDto(Agenda agenda) {
        return AgendaResponseDto.builder()
                .id(agenda.getId())
                .name(agenda.getName())
                .startTime(agenda.getStartTime())
                .duration(agenda.getDuration())
                .isOpened(verifyIfIsOpened(agenda))
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

    public static boolean verifyIfIsOpened(Agenda agenda) {
        return agenda.getStartTime() != null && (agenda.getStartTime().plusMinutes(agenda.getDuration()).isAfter(LocalDateTime.now()));
    }

    public static void verifyIfAgendaIsAlreadyOpen(Agenda agenda) {
        if (verifyIfIsOpened(agenda)) {
            log.error("Error - Agenda is already open - AgendaId: {}", agenda.getId());
            throw new AgendaIsAlreadyOpenException();
        }
    }

    public static void verifyIfAgendaHasAlreadyBeenClosed(Agenda agenda) {
        if (!verifyIfIsOpened(agenda) && agenda.getStartTime() != null) {
            log.error("Error - Agenda has already been closed - AgendaId: {}", agenda.getId());
            throw new AgendaHasAlreadyBeenClosedException();
        }
    }

    private static long calculateVotes(Agenda agenda, String choice) {
        return agenda.getVotes() == null ? 0 : agenda.getVotes().stream()
                .filter(vote -> vote.getChoice().equals(choice)).count();
    }

}
