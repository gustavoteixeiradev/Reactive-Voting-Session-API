package dev.gustavoteixeira.votingsession.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AgendaRequestDto {

    private String name;

    private int duration;

}
