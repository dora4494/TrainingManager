package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;
import fr.paloit.paloformation.service.SessionService;
import fr.paloit.paloformation.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class EmargementController {

    @Autowired
    EmargementService emargementService;

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    SessionService sessionService;

    @PostMapping("/emargement")
    public String demandeSignature(@RequestParam(name = "idSession") Long idSession, @RequestParam(name = "ids") List<Long> ids) throws IOException {
        Iterable<Utilisateur> utilisateurs = utilisateurService.listeUtilisateursById(ids);
        final Session session = sessionService.trouverSessionById(idSession);
        final EmargementService.FeuilleEmargement feuilleEmargement = emargementService.getFeuilleEmargement(session);
        emargementService.envoyerDemandeSignature(utilisateurs, feuilleEmargement);
        return "redirect:/sessions";
    }

}