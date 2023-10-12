package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SessionController {

    @Autowired
    SessionService sessionService;


    @GetMapping({"/sessions"})
    public String afficherSessions(Model model) {
        model.addAttribute("sessions", sessionService.listeSesions());
        return "sessions";
    }

    @GetMapping({"/creer-session"})
    public String creerSession(@ModelAttribute Session session, Model model) {

        return "creer-session";
    }

    @PostMapping({"/session-creee"})
    public String sessionCreee(@ModelAttribute Session session) {
        sessionService.creerSession(session);
        return "redirect:/sessions";
    }


    @GetMapping({"/session/{id}"})
    public String detailSession(Model model, @PathVariable Long id, @ModelAttribute Utilisateur utilisateur) {
        Session sessionFormation = sessionService.trouverSessionById(id);
        LocalDate dateDuJour = LocalDate.now();

        List<ToDo> cetteSemaineTodos = new ArrayList<>();
        List<ToDo> aVenirTodos = new ArrayList<>();

        for (ToDo todo : sessionFormation.getTodos()) {
            LocalDate todoDate = todo.getDate().atStartOfDay().toLocalDate();
            if (todoDate.isBefore(dateDuJour.plusDays(7))) {
                cetteSemaineTodos.add(todo);
            } else {
                aVenirTodos.add(todo);
            }
        }

        model.addAttribute("sessionFormation", sessionFormation);
        model.addAttribute("cetteSemaineTodos", cetteSemaineTodos);
        model.addAttribute("aVenirTodos", aVenirTodos);


        return "detail-session";
    }


    @PostMapping({"/supprimer-session"})
    public String supprimerSession(@RequestParam Long id) {
        Session sessionFormation = sessionService.trouverSessionById(id);
        if (sessionFormation != null) {
            sessionService.supprimerSession(sessionFormation);
        }
        return "redirect:/sessions";
    }

    @GetMapping({"/modifier-session"})
    public String modifierSession(Model model, @RequestParam Long id) {
        model.addAttribute("session", sessionService.trouverSessionById(id));
        return "creer-session";
    }


    @PostMapping({"session-modifiee"})
    public String sessionModifiee(@ModelAttribute Session session) {
        sessionService.modifierSession(session);
        return "redirect:/sessions";
    }


    @PostMapping({"/archiver-session"})
    public String archiverSession(@RequestParam Long id) {
        Session session = sessionService.trouverSessionById(id);
        if (session != null) {
            sessionService.archiverSession(session);
        }
        assert session != null;
        return "redirect:/sessions";
    }


    @PostMapping({"/annuler-session"})
    public String annulerSession(@RequestParam Long id) {
        Session session = sessionService.trouverSessionById(id);
        if (session != null) {
            sessionService.annulerSession(session);
        }
        return "redirect:/sessions";
    }

}
