package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.service.SessionService;
import fr.paloit.paloformation.service.ToDoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ControllerGlobalAdvice {

    @Autowired
    private ToDoService toDoService;

     @Autowired
     private SessionService sessionService;

    @ModelAttribute("nombreDeTodos")
    public long nombreDeTodosEnRetard() {
        return toDoService.nombreToDoAvecEcheanceDepassee();
    }

    @ModelAttribute("nombreSessionsAujourdhui")
    public long nombreDeSessionsAujourdhui() {
        return sessionService.nombreDeSessionsAujourdhui();
    }

    @ModelAttribute("nombreSessionsProchainement")
    public long nombreDeSessionsProchainement() {
        return sessionService.nombreDeSessionsProchainement();
    }


}
