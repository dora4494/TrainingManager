package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AccueilController {

    @GetMapping({"/accueil"})
    public String afficherAccueil() {
        return "accueil";
    }
}
