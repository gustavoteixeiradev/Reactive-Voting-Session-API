package dev.gustavoteixeira.votingsession.service;

import com.mongodb.DuplicateKeyException;
import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;
import dev.gustavoteixeira.votingsession.entity.Vote;
import dev.gustavoteixeira.votingsession.exception.AgendaHasAlreadyBeenClosedException;
import dev.gustavoteixeira.votingsession.exception.AgendaIsAlreadyOpenException;
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

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        var result = service.createAgenda(request);

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

        var result = service.createAgenda(request);

        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof CreatingAgendaException)
                .verify();
    }

    @Test
    public void shouldGetAgenda() {
        var agendaId = "1";

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder().id(agendaId).name("A random name").duration(1).build()));

        var result = service.getAgenda(agendaId);

        StepVerifier
                .create(result)
                .expectNextMatches(agendaResponseDto -> {
                    assertEquals(agendaResponseDto.getId(), agendaId);
                    assertEquals(agendaResponseDto.getName(), "A random name");
                    assertEquals(agendaResponseDto.getDuration(), 1);
                    assertEquals(agendaResponseDto.getPositiveVotes(), 0);
                    assertEquals(agendaResponseDto.getNegativeVotes(), 0);
                    assertFalse(agendaResponseDto.isOpened());
                    assertNull(agendaResponseDto.getStartTime());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void shouldGetAgendaWithVotes() {
        var startedTime = LocalDateTime.now().minusMinutes(2);
        var agendaId = "1";

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder()
                        .id(agendaId)
                        .name("A random name")
                        .duration(10)
                        .startTime(startedTime)
                        .votes(Arrays.asList(
                                Vote.builder().associate("123").choice("Sim").build(),
                                Vote.builder().associate("321").choice("Não").build()))
                        .build()));

        var result = service.getAgenda(agendaId);

        StepVerifier
                .create(result)
                .expectNextMatches(agendaResponseDto -> {
                    assertEquals(agendaResponseDto.getId(), agendaId);
                    assertEquals(agendaResponseDto.getName(), "A random name");
                    assertEquals(agendaResponseDto.getDuration(), 10);
                    assertEquals(agendaResponseDto.getPositiveVotes(), 1);
                    assertEquals(agendaResponseDto.getNegativeVotes(), 1);
                    assertEquals(agendaResponseDto.getStartTime(), startedTime);
                    assertTrue(agendaResponseDto.isOpened());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void shouldStartAgenda() {
        var startedTime = LocalDateTime.now();
        var agendaId = "1";

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder().id(agendaId).name("A random name").duration(1).build()));

        when(repository.save(any()))
                .thenReturn(Mono.just(Agenda.builder()
                        .id(agendaId)
                        .name("A random name")
                        .startTime(startedTime)
                        .duration(1)
                        .build()));

        var result = service.startAgenda(agendaId);

        StepVerifier
                .create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void shouldNotStartAgendaIfItIsAlreadyOpen() {
        var startedTime = LocalDateTime.now().minusMinutes(5);
        var agendaId = "1";

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder()
                        .id(agendaId)
                        .startTime(startedTime)
                        .name("A random name")
                        .duration(10).build()));

        var result = service.startAgenda(agendaId);

        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof AgendaIsAlreadyOpenException)
                .verify();
    }

    @Test
    public void shouldNotStartAgendaIfItHasAlreadyBeenClosed() {
        var startedTime = LocalDateTime.now().minusMinutes(10);
        var agendaId = "1";

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder()
                        .id(agendaId)
                        .startTime(startedTime)
                        .name("A random name")
                        .duration(5).build()));

        var result = service.startAgenda(agendaId);

        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof AgendaHasAlreadyBeenClosedException)
                .verify();
    }

}
