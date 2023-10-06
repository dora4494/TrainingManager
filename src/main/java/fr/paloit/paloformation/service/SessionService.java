package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Tache;
import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.repository.SessionRepository;
import fr.paloit.paloformation.repository.TacheRepository;
import fr.paloit.paloformation.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    TacheRepository tacheRepository;

    @Autowired
    ToDoService toDoService;

    public Iterable<Session> listeSesions() {
        return sessionRepository.findAll();
    }

    public void creerSession(Session session) {
        sessionRepository.save(session);
        toDoService.creerTodos(session);
    }

    public void supprimerSession(Session session) {
        sessionRepository.delete(session);
    }

    public Session trouverSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable avec l'ID : " + id));
    }





    // Etat 1 : En cours , Etat 3 : Archivée
    public void archiverSession(Session session) {

        LocalDate dateDuJour = LocalDate.now();
        if (session.getEtat() == 1 && session.getDateDebut().isAfter(dateDuJour)) {
            session.setEtat(3);
            sessionRepository.save(session);
        }
    }

    public void modifierSession(Session session) {
        if (session.getEtat() == 1) {
            sessionRepository.save(session);
        }
    }

    // Etat 1 : En cours , Etat  : Annulée
    public void annulerSession(Session session) {
        if (session.getEtat() == 1) {
            session.setEtat(2);
            sessionRepository.save(session);
        }
    }


}
