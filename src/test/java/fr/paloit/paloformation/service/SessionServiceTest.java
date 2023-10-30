package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.repository.SessionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SessionServiceTest {

    @Autowired
    SessionService sessionService;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    FormationService formationService;

    @Autowired
    ToDoService toDoService;

    @Test
    public void testAfficherSessionsSiDatesNonRenseignees() throws Exception {
        initDB();

        Set<Utilisateur> participants = new HashSet<>();
        List<ToDo> todos = new ArrayList<>();
        Set<LocalDate> dates = new HashSet<>();
        Session session = new Session(null, getUneFormation(), "client", participants, todos, getUnFormateur(), 2, 4L, "modalites", 1, dates, null);


        sessionService.creerSession(session);
        final Iterable<Session> sessions = sessionService.listeSesions();
        final long countSession = StreamSupport.stream(sessions.spliterator(), false).count();
        assertEquals(1, countSession);

    }



    @Test
    public void testAfficherSessionsSiDatesRenseignees() throws Exception {
        initDB();

        Set<Utilisateur> participants = new HashSet<>();
        List<ToDo> todos = new ArrayList<>();
        Set<LocalDate> dates =  Set.of(LocalDate.of(2023,11,21));
        Session session = new Session(null, getUneFormation(), "client", participants, todos, getUnFormateur(), 2, 4L, "modalites", 1, dates, null);

        sessionService.creerSession(session);

        final Iterable<Session> sessions = sessionService.listeSesions();
        final long countSession = StreamSupport.stream(sessions.spliterator(), false).count();
        assertEquals(1, countSession);
        final Iterable<ToDo> lstTodo = toDoService.listeToDo();
        final long countToDo = StreamSupport.stream(lstTodo.spliterator(), false).count();
        assertEquals(2, countToDo);

    }

    private Formation getUneFormation() {
        return formationService.listeFormations().iterator().next();
    }

    private void initDB() {
        Utilisateur formateur = new Utilisateur(null, "Jean", "Bodier", Arrays.asList().toString());
        utilisateurService.creerUtilisateur(formateur);

        Formation formation = new Formation(null, "formation", null);
        formationService.creerFormation(formation);
    }

    private Utilisateur getUnFormateur() {
        return utilisateurService.listeUtilisateurs().iterator().next();
    }


}
