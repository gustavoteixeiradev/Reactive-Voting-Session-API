package dev.gustavoteixeira.votingsession.repository;

import dev.gustavoteixeira.votingsession.entity.Agenda;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends ReactiveMongoRepository<Agenda, String> {
}
