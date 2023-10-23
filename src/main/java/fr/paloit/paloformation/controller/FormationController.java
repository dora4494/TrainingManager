package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.service.FormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class FormationController {

    @Autowired
    FormationService formationService;


    @GetMapping({"/formations"})
    public String afficherFormations(Model model) {
        model.addAttribute("formations", formationService.listeFormations());
        return "formations";
    }


    @GetMapping({"/creer-formation"})
    public String creerFormation(@ModelAttribute Formation formation, Model model) {
        return "creer-formation";
    }



@PostMapping({"/formation-cree"})
    public String formationCreee(@ModelAttribute Formation formation){
        formationService.creerFormation(formation);
        return "redirect:/formations";
    }

    @GetMapping({"/formation/{id}"})
    public String detailFormation(Model model, @PathVariable Long id) {
        model.addAttribute("formation", formationService.trouverFormationById(id));
        return "detail-formation";
    }

    @PostMapping("/supprimer-formation")
    public String supprimerFormation(@RequestParam Long id) {
        Formation formation = formationService.trouverFormationById(id);
        if (formation != null) {
            formationService.supprimerFormation(formation);
        }
        return "redirect:/formations";
    }



    @GetMapping({"/modifier-formation"})
    public String modifierFormation(@RequestParam Long id, Model model) {
        model.addAttribute("formation", formationService.trouverFormationById(id));
        return "creer-formation";
    }

}
