package fr.paloit.paloformation.controller;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErreurController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "erreur";
    }
}
