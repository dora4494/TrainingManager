package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;
import fr.paloit.paloformation.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class DocusignController {


    @Autowired
    EmargementService docusignService;

    @Autowired
    UtilisateurService utilisateurService;


    @GetMapping("/docusign")
    public String demandeSignature(@RequestParam(name = "ids") List<Long> ids) throws IOException {
        Iterable<Utilisateur> utilisateurs = utilisateurService.listeUtilisateursById(ids);
        docusignService.envoyerDemandeSignature(utilisateurs);
        return "redirect:/sessions";
    }

}