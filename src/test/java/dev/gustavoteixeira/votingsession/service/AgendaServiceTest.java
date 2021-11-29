package dev.gustavoteixeira.votingsession.service;

import com.mongodb.DuplicateKeyException;
import dev.gustavoteixeira.votingsession.client.UserInfoClient;
import dev.gustavoteixeira.votingsession.constants.UserStatus;
import dev.gustavoteixeira.votingsession.dto.UserInfoDto;
import dev.gustavoteixeira.votingsession.dto.request.AgendaRequestDto;
import dev.gustavoteixeira.votingsession.dto.request.VoteRequestDto;
import dev.gustavoteixeira.votingsession.entity.Agenda;
import dev.gustavoteixeira.votingsession.entity.Vote;
import dev.gustavoteixeira.votingsession.exception.*;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class AgendaServiceTest {

    @InjectMocks
    private AgendaServiceImpl service;

    @Mock
    private AgendaRepository repository;

    @Mock
    private UserInfoClient userInfoClient;

    @Test
    void shouldCreateAgenda() {
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
                        assertEquals("1", id))
                .verifyComplete();
    }

    @Test
    void shouldThrowCreatingAgendaException() {
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
    void shouldGetAgenda() {
        var agendaId = "1";

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder().id(agendaId).name("A random name").duration(1).build()));

        var result = service.getAgenda(agendaId);

        StepVerifier
                .create(result)
                .expectNextMatches(agendaResponseDto -> {
                    assertEquals(agendaId, agendaResponseDto.getId());
                    assertEquals("A random name", agendaResponseDto.getName());
                    assertEquals(1, agendaResponseDto.getDuration());
                    assertEquals(0, agendaResponseDto.getPositiveVotes());
                    assertEquals(0, agendaResponseDto.getNegativeVotes());
                    assertFalse(agendaResponseDto.isOpened());
                    assertNull(agendaResponseDto.getStartTime());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldGetAgendaWithVotes() {
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
                    assertEquals(agendaId, agendaResponseDto.getId());
                    assertEquals("A random name", agendaResponseDto.getName());
                    assertEquals(10, agendaResponseDto.getDuration());
                    assertEquals(1, agendaResponseDto.getPositiveVotes());
                    assertEquals(1, agendaResponseDto.getNegativeVotes());
                    assertEquals(startedTime, agendaResponseDto.getStartTime());
                    assertTrue(agendaResponseDto.isOpened());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldStartAgenda() {
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
    void shouldNotStartAgendaIfItIsAlreadyOpen() {
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
    void shouldNotStartAgendaIfItHasAlreadyBeenClosed() {
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

    @Test
    void shouldVote() {
        var agendaId = "1";
        var voteRequest = Mono.just(
                VoteRequestDto.builder()
                        .associate("123")
                        .choice("Sim").build());

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder()
                        .id(agendaId)
                        .startTime(LocalDateTime.now())
                        .name("A random name")
                        .duration(5).build()));

        when(userInfoClient.getUserInfo(any()))
                .thenReturn(Mono.just(UserInfoDto.builder()
                        .status(UserStatus.ABLE_TO_VOTE).build()));
        when(repository.save(any()))
                .thenReturn(Mono.just(Agenda.builder().build()));

        var result = service.voteAgenda(agendaId, voteRequest);

        StepVerifier
                .create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldNotVoteOnAgendaIfItIsNotOpen() {
        var agendaId = "1";
        var voteRequest = Mono.just(
                VoteRequestDto.builder()
                        .associate("123")
                        .choice("Sim").build());

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder()
                        .id(agendaId)
                        .startTime(null)
                        .name("A random name")
                        .duration(5).build()));

        var result = service.voteAgenda(agendaId, voteRequest);

        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof AgendaIsNotOpenException)
                .verify();
    }

    @Test
    void shouldNotVoteOnAgendaIfItIsAlreadyClosed() {
        var agendaId = "1";
        var voteRequest = Mono.just(
                VoteRequestDto.builder()
                        .associate("123")
                        .choice("Sim").build());

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder()
                        .id(agendaId)
                        .startTime(LocalDateTime.now())
                        .name("A random name")
                        .duration(5).build()));

        when(userInfoClient.getUserInfo(any()))
                .thenReturn(Mono.just(UserInfoDto.builder()
                        .status(UserStatus.UNABLE_TO_VOTE).build()));

        var result = service.voteAgenda(agendaId, voteRequest);

        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof AssociateIsNotAbleToVoteException)
                .verify();
    }

    @Test
    void shouldNotVoteOnAgendaIfAssociateAlreadyVoted() {
        var agendaId = "1";
        var voteRequest = Mono.just(
                VoteRequestDto.builder()
                        .associate("123")
                        .choice("Sim").build());

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(Agenda.builder()
                        .id(agendaId)
                        .startTime(LocalDateTime.now())
                        .name("A random name")
                        .votes(List.of(Vote.builder().associate("123").choice("Sim").build()))
                        .duration(5).build()));

        when(userInfoClient.getUserInfo(any()))
                .thenReturn(Mono.just(UserInfoDto.builder()
                        .status(UserStatus.ABLE_TO_VOTE).build()));

        var result = service.voteAgenda(agendaId, voteRequest);

        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof VoteAlreadyExistsException)
                .verify();
    }

}
