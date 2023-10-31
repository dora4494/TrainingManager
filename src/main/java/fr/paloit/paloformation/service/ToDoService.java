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
import java.util.Set;

@Service
public class ToDoService {

    @Autowired
    TacheRepository tacheRepository;

    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    SessionRepository sessionRepository;

    public void creerTodos(Session session) {

        Tache conventionFormation = tacheRepository.findById(1L).orElse(null);
        Tache bloquerAgendaFormateur = tacheRepository.findById(2L).orElse(null);
        Tache demanderListeParticipants = tacheRepository.findById(3L).orElse(null);
        Tache inviterParticipants = tacheRepository.findById(4L).orElse(null);
        Tache creerFeuilleEmargement = tacheRepository.findById(5L).orElse(null);
        Tache creerAttestationFormation = tacheRepository.findById(6L).orElse(null);
        Tache envoyerFeuilleEmargement = tacheRepository.findById(7L).orElse(null);
        Tache envoyerQuestionnaire = tacheRepository.findById(8L).orElse(null);
        Tache transmettreAttestationFormation = tacheRepository.findById(9L).orElse(null);


        LocalDate dateLaPlusGrande = session.getDates().stream().max(Comparator.naturalOrder()).orElse(null);

        LocalDate dateCreationSession = session.getDateCreation();

        Set<LocalDate> datesSession = session.getDates();

        if (conventionFormation == null || bloquerAgendaFormateur == null
        ||demanderListeParticipants == null || inviterParticipants == null
        || creerFeuilleEmargement == null || creerAttestationFormation == null
        || envoyerFeuilleEmargement == null || envoyerQuestionnaire == null
        || transmettreAttestationFormation == null) {
            throw new RuntimeException("Erreur: tâches introuvables");
        }

        ToDo conventionF = new ToDo();
        conventionF.setTache(conventionFormation);
        conventionF.setSession(session);
        conventionF.setDate(dateCreationSession);

        ToDo bloquerAgendaF = new ToDo();
        bloquerAgendaF.setTache(bloquerAgendaFormateur);
        bloquerAgendaF.setSession(session);
        bloquerAgendaF.setDate(dateCreationSession);

        ToDo listeParticipants = new ToDo();
        listeParticipants.setTache(demanderListeParticipants);
        listeParticipants.setSession(session);
        listeParticipants.setDate(dateCreationSession);

        ToDo inviterPart = new ToDo();
        inviterPart.setTache(inviterParticipants);
        inviterPart.setSession(session);
        inviterPart.setDate(dateCreationSession.plusDays(7));

        ToDo creerFeuilleE = new ToDo();
        creerFeuilleE.setTache(creerFeuilleEmargement);
        creerFeuilleE.setSession(session);
        creerFeuilleE.setDate(dateCreationSession.plusDays(9));

        ToDo creerAttestionF = new ToDo();
        creerAttestionF.setTache(creerAttestationFormation);
        creerAttestionF.setSession(session);
        creerAttestionF.setDate(dateCreationSession.plusDays(9));




        for (LocalDate dateDeSession : datesSession) {
            ToDo envoyerFeuilleE = new ToDo();
            envoyerFeuilleE.setTache(envoyerFeuilleEmargement);
            envoyerFeuilleE.setSession(session);

            LocalDate dateToDo = dateDeSession;
            envoyerFeuilleE.setDate(dateToDo);

            toDoRepository.save(envoyerFeuilleE);
        }

        ToDo envoyerQuest = new ToDo();
        envoyerQuest.setTache(envoyerQuestionnaire);
        envoyerQuest.setSession(session);
        envoyerQuest.setDate(dateLaPlusGrande.plusDays(1));

        ToDo transmettreAttestationF = new ToDo();
        transmettreAttestationF.setTache(transmettreAttestationFormation);
        transmettreAttestationF.setSession(session);
        transmettreAttestationF.setDate(dateLaPlusGrande.plusDays(1));

        toDoRepository.save(conventionF);
        toDoRepository.save(bloquerAgendaF);
        toDoRepository.save(listeParticipants);
        toDoRepository.save(inviterPart);
        toDoRepository.save(creerFeuilleE);
        toDoRepository.save(creerAttestionF);
        toDoRepository.save(envoyerQuest);
        toDoRepository.save(transmettreAttestationF);


    }


    public ToDo trouverToDoById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable avec l'ID : " + id));
    }


    public Iterable<ToDo> listeToDo() {
        return toDoRepository.findAll();
    }


    public void modifierEtat(ToDo todo) {
        if (todo.getEtat() == ToDo.Etat.FAIT) {
            todo.setEtat(ToDo.Etat.A_FAIRE);
        } else {
            todo.setEtat(ToDo.Etat.FAIT);
        }
        toDoRepository.save(todo);
    }


}
