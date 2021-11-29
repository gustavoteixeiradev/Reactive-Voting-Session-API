package dev.gustavoteixeira.votingsession.dto;

import dev.gustavoteixeira.votingsession.constants.UserStatus;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {

    private UserStatus status;

}
