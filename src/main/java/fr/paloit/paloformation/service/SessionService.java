package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;

    public Iterable<Session> listeSesions() {
        return sessionRepository.findAll();
    }

    public void creerSession(Session session) {
        sessionRepository.save(session);
    }

    public void supprimerSession(Session session) {
        sessionRepository.delete(session);
    }

    public Session trouverSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable avec l'ID : " + id));
    }

}
