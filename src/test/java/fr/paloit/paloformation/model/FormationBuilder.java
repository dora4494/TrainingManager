package fr.paloit.paloformation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormationBuilder {
    private Formation formation;

    public static FormationBuilder uneFormation(String intitule) {
        return new FormationBuilder(new Formation(35L, intitule, new ArrayList<Session>()));
    }

    public FormationBuilder(Formation formation) {
        this.formation = formation;
    }

    public FormationBuilder setId(Long id) {
        formation.setId(id);
        return this;
    }

    public FormationBuilder setIntitule(String intitule) {
        formation.setIntitule(intitule);
        return this;
    }

    public FormationBuilder setSessions(List<Session> sessions) {
        positionnerFormation(sessions);
        formation.setSessions(sessions);
        return this;
    }

    public FormationBuilder addSessions(Session... sessions) {
        final List<Session> listeSessions = Arrays.asList(sessions);
        positionnerFormation(listeSessions);
        formation.getSessions().addAll(listeSessions);
        return this;
    }

    public Formation get() {
        return formation;
    }

    private void positionnerFormation(List<Session> sessions) {
        sessions.forEach(session -> session.setFormation(formation));
    }
}