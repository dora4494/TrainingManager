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
import java.util.Comparator;
import java.util.List;

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

        LocalDate dateCreationSession = LocalDate.now();

        session.setDateCreation(dateCreationSession);

        sessionRepository.save(session);
        if (session.getDates() != null && !session.getDates().isEmpty()) {
        toDoService.creerTodos(session);
        }
    }

    public void supprimerSession(Session session) {
        sessionRepository.delete(session);
    }

    public Session trouverSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable avec l'ID : " + id));
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

    public long nombreDeSessionsAujourdhui() {
        List<Session> sessions = (List<Session>) sessionRepository.findAll();
        long nbDeSessionsAujourdhui = sessions.stream()
                .filter(session -> session.getDates().stream().anyMatch(date -> date.isEqual(LocalDate.now())))
                .count();
        return nbDeSessionsAujourdhui;
    }


    public long nombreDeSessionsProchainement() {
        List<Session> sessions = (List<Session>) sessionRepository.findAll();
        long nbDeSessionsProchainement = sessions.stream()
                .filter(session -> session.getDates().stream().anyMatch(date -> date.isAfter(LocalDate.now())))
                .count();
        return nbDeSessionsProchainement;
    }

}





