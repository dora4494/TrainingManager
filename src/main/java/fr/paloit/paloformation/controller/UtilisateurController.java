package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UtilisateurController {

    @Autowired
    UtilisateurService utilisateurService;

    @GetMapping({"/liste-utilisateurs"})
    public String afficherUtilisateurs(Model model) {
        model.addAttribute("utilisateurs", utilisateurService.listeUtilisateurs());
        return "liste-utilisateurs";
    }

    @GetMapping({"/creer-utilisateur"})
    public String creerUtilisateur(@ModelAttribute Utilisateur utilisateur, Model model) {

        return "creer-utilisateur";
    }


    @PostMapping({"/utilisateur-cree"})
    public String utilisateurCree(@ModelAttribute Utilisateur utilisateur) {
        utilisateurService.creerUtilisateur(utilisateur);
        return "redirect:/liste-utilisateurs";
    }


    @GetMapping({"/utilisateur/{id}"})
    public String detailUtilisateur(Model model, @PathVariable Long id) {
        model.addAttribute("utilisateur", utilisateurService.trouverUtilisateurById(id));
        return "detail-utilisateur";
    }


    @PostMapping("/supprimer-utilisateur")
    public String supprimerUtilisateur(@RequestParam Long id) {
        Utilisateur utilisateur = utilisateurService.trouverUtilisateurById(id);
        if (utilisateur != null) {
            utilisateurService.supprimerUtilisateur(utilisateur);
        }
        return "redirect:/liste-utilisateurs";
    }

    @GetMapping({"/modifier/utilisateur/{id}"})
    public String modifierUtilisateur(Model model, @PathVariable Long id) {
        model.addAttribute("utilisateur", utilisateurService.trouverUtilisateurById(id));
        return "creer-utilisateur";
    }


}
