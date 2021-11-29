package dev.gustavoteixeira.votingsession.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class AgendaResponseDto {

    private String id;

    private String name;

    private LocalDateTime startTime;

    private int duration;

    private boolean isOpened;

    private long positiveVotes;

    private long negativeVotes;

}
