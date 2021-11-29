package dev.gustavoteixeira.votingsession.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaRequestDto {

    private String name;

    private int duration;

}
