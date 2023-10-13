package fr.paloit.paloformation.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formation")
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String intitule;

    @OneToMany(mappedBy = "formation")
    private List<Session> sessions = new ArrayList<>();

    public Formation(Long id, String intitule, List<Session> sessions) {
        this.id = id;
        this.intitule = intitule;
        this.sessions = sessions;
    }

    public Formation() {
    }

    public Formation(boolean b, Object o) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }


    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", intitule='" + intitule + '\'' +
                '}';
    }


    public static class Resultat {
        private boolean ok;
        private String message;

        public Resultat(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }

        public boolean isOk() {

            return ok;
        }

        public String erreur() {

            return message;
        }
    }
}
