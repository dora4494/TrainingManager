package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class DocusignController {


    @Autowired
    DocusignService docusignService;

    @Autowired
    UtilisateurService utilisateurService;

    @GetMapping({"/docusign"})
    public String envoyerDemandeSignature(@RequestParam Long id, Model model) throws IOException {
        Utilisateur utilisateur = utilisateurService.trouverUtilisateurById(id);
        docusignService.envoyerDemandeSignature(utilisateur);
        return "docusign";
    }

}
