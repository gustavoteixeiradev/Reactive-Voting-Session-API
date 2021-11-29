package dev.gustavoteixeira.votingsession.exception.handler;

import dev.gustavoteixeira.votingsession.exception.AgendaHasAlreadyBeenClosedException;
import dev.gustavoteixeira.votingsession.exception.AgendaIsAlreadyOpenException;
import dev.gustavoteixeira.votingsession.exception.CreatingAgendaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {

    @ExceptionHandler(CreatingAgendaException.class)
    public ResponseEntity<String> handleCreatingAgendaException(CreatingAgendaException e) {
        return ResponseEntity
                .badRequest().body("Error while creating the agenda.");
    }

    @ExceptionHandler(AgendaIsAlreadyOpenException.class)
    public ResponseEntity<String> handleAgendaIsAlreadyOpenException(AgendaIsAlreadyOpenException e) {
        return ResponseEntity
                .badRequest().body("Agenda Is Already Open.");
    }

    @ExceptionHandler(AgendaHasAlreadyBeenClosedException.class)
    public ResponseEntity<String> handleAgendaHasAlreadyBeenClosedException(AgendaHasAlreadyBeenClosedException e) {
        return ResponseEntity
                .badRequest().body("Agenda Has Already Been Closed.");
    }

}
