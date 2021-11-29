package dev.gustavoteixeira.votingsession.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Document(collection = "agendas")
public class Agenda {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private LocalDateTime startTime;

    private int duration;

    private List<Vote> votes;

}
