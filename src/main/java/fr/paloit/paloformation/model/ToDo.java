package fr.paloit.paloformation.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "todo")
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tache_id")
    private Tache tache;
    private LocalDate date;

    private int etat = 1;


    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public ToDo(Long id, Tache tache, LocalDate date, int etat, Session session) {
        this.id = id;
        this.tache = tache;
        this.date = date;
        this.etat = etat;
        this.session = session;
    }

    public ToDo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", tache=" + tache +
                ", date=" + date +
                ", etat=" + etat +
                ", session=" + session +
                '}';
    }

    public Etat getEtatb() {
        return Etat.valueOf(etat);
    }

    public void setEtatb(Etat etat) {
        this.etat = etat.id;
    }

    public enum Etat {
        A_FAIRE(1),FAIT(2);

        private final int id;

        // TODO on ne devrait pas en avoir besoin
        public int getId() {
            return id;
        }

        Etat(int id) {
            this.id = id;
        }

        private static Etat valueOf(int etatId) {
            for (Etat etat : values()) {
                if (etat.id == etatId) {
                    return etat;
                }
            }
            return null;
        }
    }


}
