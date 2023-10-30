package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.SessionService;
import fr.paloit.paloformation.service.UtilisateurService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;

@Controller
public class SessionController {

    @Autowired
    private SessionService sessionService;


    @GetMapping({"/sessions"})
    public String afficherSessions(Model model) {
        System.out.println("SessionController.afficherSessions");
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

        List<ToDo> toDosTriees = sessionFormation.getTodos().stream()
                .sorted(Comparator.comparing(ToDo::getDate))
                .collect(Collectors.toList());

        sessionFormation.setTodos(toDosTriees);

        model.addAttribute("sessionFormation", sessionFormation);
        return "detail-session";
    }


    @PostMapping({"/supprimer-session"})
    public String supprimerSession(@RequestParam Long id) {
        Session session = sessionService.trouverSessionById(id);
        if (session != null) {
            sessionService.supprimerSession(session);
        }
        return "redirect:/sessions";
    }

    @GetMapping({"/modifier-session"})
    public String modifierSession(Model model, @RequestParam Long id) {
        Session sessionFormation = sessionService.trouverSessionById(id);
        model.addAttribute("sessionFormation", sessionFormation);
        return "modifier-session";
    }


    @PostMapping({"session-modifiee"})
    public String sessionModifiee(@ModelAttribute Session session, Model model) {
        sessionService.modifierSession(session);
        return "redirect:/session/" + session.getId();
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



