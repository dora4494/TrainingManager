package fr.paloit.paloformation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ParametreController {

    @GetMapping({"/parametres"})
    public String afficherParametres() {
        return "parametres";
    }




}
