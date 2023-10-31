package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;
import fr.paloit.paloformation.service.SessionService;
import fr.paloit.paloformation.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class DocuSignController {


    @Autowired
    EmargementService docusignService;

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    SessionService sessionService;

    @PostMapping("/docusign")
    public String demandeSignature(@RequestParam(name = "idSession") Long idSession, @RequestParam(name = "ids") List<Long> ids) throws IOException {
        Iterable<Utilisateur> utilisateurs = utilisateurService.listeUtilisateursById(ids);
        final Session session = sessionService.trouverSessionById(idSession);
        final EmargementService.FeuilleEmargement feuilleEmargement = docusignService.getFeuilleEmargement(session);
        docusignService.envoyerDemandeSignature(utilisateurs, feuilleEmargement);
        return "redirect:/sessions";
    }

}