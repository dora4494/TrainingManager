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

    private int etatId = 1;


    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public ToDo(Long id, Tache tache, LocalDate date, int etat, Session session) {
        this.id = id;
        this.tache = tache;
        this.date = date;
        this.etatId = etat;
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

    @Column(name = "etat")
    private int getEtatId() {
        return etatId;
    }

    private void setEtatId(int etatId) {
        this.etatId = etatId;
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
                ", etat=" + etatId +
                ", session=" + session +
                '}';
    }

    public Etat getEtat() {
        return Etat.valueOf(etatId);
    }

    public void setEtat(Etat etat) {
        this.etatId = etat.id;
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
