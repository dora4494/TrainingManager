package fr.paloit.paloformation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class TrefleioApi {

    @GetMapping("/strawberry")
    public ResponseEntity<String> afficherDonneesBeachStrawberry() {
        try {

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> reponse = restTemplate.getForEntity("http://localhost:8081/api/beach-strawberry", String.class);

            if (reponse.getStatusCode() == HttpStatus.OK) {

                return ResponseEntity.ok(reponse.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errorstatus");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errorexception");
        }


    }
}
