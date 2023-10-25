package fr.paloit.paloformation.controller;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class ErreurController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        final HttpStatus statusCode = Optional.of(request)
                .map(r -> r.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
                .map(status -> HttpStatus.valueOf(Integer.valueOf(status.toString())))
                .orElse(null);

        if (statusCode == HttpStatus.NOT_FOUND) {
            return "erreur_introuvable";
        }
        if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            return "erreur_technique";
        }
        return "erreur_technique";
    }
}
