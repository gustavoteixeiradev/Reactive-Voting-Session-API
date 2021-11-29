package dev.gustavoteixeira.votingsession.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Vote {

    private String associate;

    private String choice;

}
