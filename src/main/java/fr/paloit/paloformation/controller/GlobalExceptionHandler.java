package fr.paloit.paloformation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Permet d'intercepter les erreurs et rediriger vers la page d'erreur.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public String  handleRuntimeException(RuntimeException exception) {
        exception.printStackTrace();
        return "erreur_technique";
    }
}
