package dev.gustavoteixeira.votingsession.service;

import com.mongodb.DuplicateKeyException;
import dev.gustavoteixeira.votingsession.dto.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;
import dev.gustavoteixeira.votingsession.exception.CreatingAgendaException;
import dev.gustavoteixeira.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.votingsession.service.impl.AgendaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class AgendaServiceTest {

    @InjectMocks
    private AgendaServiceImpl service;

    @Mock
    private AgendaRepository repository;


    @Test
    public void shouldCreateAgenda() {
        var request = Mono.just(AgendaRequestDto.builder()
                .name("A random name")
                .duration(1)
                .build());

        when(repository.insert(any(Agenda.class)))
                .thenReturn(Mono.just(Agenda.builder().id("1").name("A random name").duration(1).build()));

        Mono<String> result = service.createAgenda(request);

        StepVerifier
                .create(result)
                .consumeNextWith(id ->
                        assertEquals(id, "1"))
                .verifyComplete();
    }

    @Test
    public void shouldThrowCreatingAgendaException() {
        var request = Mono.just(AgendaRequestDto.builder()
                .name("A random name")
                .duration(1)
                .build());

        when(repository.insert(any(Agenda.class)))
                .thenThrow(DuplicateKeyException.class);

        Mono<String> result = service.createAgenda(request);

        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof CreatingAgendaException)
                .verify();
    }

}
