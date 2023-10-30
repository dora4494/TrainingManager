package fr.paloit.paloformation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessionBuilder {
    private Session session;

    public static SessionBuilder uneSession() {
        Formation formation = null;
        return new SessionBuilder(new Session(23L, formation, "client", new HashSet<Utilisateur>(), new ArrayList<ToDo>(), null, 2, 230L, "modalites", 0, new HashSet<>(), null));
    }

    public static SessionBuilder uneSession(String intituleFormation) {
        final Formation formation = FormationBuilder.uneFormation(intituleFormation).get();
        final SessionBuilder sessionBuilder = uneSession().setFormation(formation);
        return sessionBuilder;
    }

    public SessionBuilder(Session session) {
        this.session = session;
    }

    public SessionBuilder setId(Long id) {
        session.setId(id);
        return this;
    }

    public SessionBuilder setFormation(Formation formation) {
        session.setFormation(formation);
        if (!formation.getSessions().contains(session)) {
            formation.getSessions().add(session);
        }
        return this;
    }

    public SessionBuilder setClient(String client) {
        session.setClient(client);
        return this;
    }

    public SessionBuilder setParticipants(Set<Utilisateur> participants) {
        session.setParticipants(participants);
        return this;
    }

    public SessionBuilder setTodos(List<ToDo> todos) {
        session.setTodos(todos);
        return this;
    }

    public SessionBuilder setFormateur(Utilisateur formateur) {
        session.setFormateur(formateur);
        return this;
    }

    public SessionBuilder setDuree(int duree) {
        session.setDuree(duree);
        return this;
    }

    public SessionBuilder setCout(Long cout) {
        session.setCout(cout);
        return this;
    }

    public SessionBuilder setModalites(String modalites) {
        session.setModalites(modalites);
        return this;
    }

    public SessionBuilder setEtat(int etat) {
        session.setEtat(etat);
        return this;
    }

    public SessionBuilder setDates(Set<LocalDate> dates) {
        session.setDates(dates);
        return this;
    }

    public Session get() {
        return session;
    }
}