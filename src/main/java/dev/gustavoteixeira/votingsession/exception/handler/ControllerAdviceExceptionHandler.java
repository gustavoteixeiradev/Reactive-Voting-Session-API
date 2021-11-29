package dev.gustavoteixeira.votingsession.exception.handler;

import dev.gustavoteixeira.votingsession.exception.*;
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

    @ExceptionHandler(AssociateIsNotAbleToVoteException.class)
    public ResponseEntity<String> handleAssociateIsNotAbleToVoteException(AssociateIsNotAbleToVoteException e) {
        return ResponseEntity
                .badRequest().body("Associate Is Not Able To Vote.");
    }

    @ExceptionHandler(VoteAlreadyExistsException.class)
    public ResponseEntity<String> handleVoteAlreadyExistsException(VoteAlreadyExistsException e) {
        return ResponseEntity
                .badRequest().body("Vote Already Exists");
    }

}
