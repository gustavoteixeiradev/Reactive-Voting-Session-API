package dev.gustavoteixeira.votingsession.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteRequestDto {

    private String associate;

    private String choice;

}
