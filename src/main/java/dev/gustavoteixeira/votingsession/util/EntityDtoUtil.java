package dev.gustavoteixeira.votingsession.util;

import dev.gustavoteixeira.votingsession.dto.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;

public class EntityDtoUtil {

    public static final int DEFAULT_DURATION = 1;

    public static AgendaRequestDto toDto(Agenda agenda) {
        return AgendaRequestDto.builder()
                .name(agenda.getName())
                .duration(agenda.getDuration() <= 0 ? DEFAULT_DURATION : agenda.getDuration())
                .build();
    }

    public static Agenda toEntity(AgendaRequestDto agendaDto) {
        return Agenda.builder()
                .name(agendaDto.getName())
                .duration(agendaDto.getDuration())
                .build();
    }


}
